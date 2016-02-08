package com.panelcomposer.components;


import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.panelcomoser.components.exception.NegativeValueException;

/**
 * Komponenta omogucava unos iznosa u odgovarajucem formatu
 * Korisnik pocinje kucanje od kraja, i broj se pomera kako se unose cifre
 * Moguce je zadati broj decimala, i ukupan broj cifara, na osnovu cega se formira zeljeni izgled 
 * Pritiskom tastera - se pojavljuje dati znak da pocetku dok taj efekat ponistava taster +
 * Metoda getValue vraca unetu vrednost kao BigDecimal
 * Metoda setValue postavlja sadrzaj polja
 */
public class DecimalTextField extends JTextField implements IValidationTextField{
	private static final long serialVersionUID = 1L;

	public static final String TEXT_PROPERTY = "text";

	/**
	 * CTRL modifier
	 */
	public static final int CTRL_MASK = InputEvent.CTRL_MASK;
	/**
	 * Srpski lokal
	 */
	public static final String locale="sr";
	/**
	 * Za formatiranje iznosa u polju
	 */
	private DecimalFormat formatter;
	/**
	 * Broj decimalnih mesta, cifara, tacaka
	 */
	private int brojDecimalnihMesta,brojCifaraPreZareza,brojTacaka;
	/**
	 * Broj cifara pre prve tacke, koliko mesta ima do znaka
	 */
	private int brojCifaraNaPocetku;
	/**
	 * Znak minus za negativne brojeve i nista za pozitivne
	 */
	private char znak=' ';
	/**
	 * Odredjuje da li se treba "lepiti" - da li je pritisnuto ctrl+v
	 */
	private boolean paste=false;
	/**
	 * Tekst koji se nalazio pre "lepljenja", kako bi se moglo da njega vratiti ako nije ispravno to sto se pastuje
	 */
	private String stariText;
	/**
	 * Tekst koji ce predstaljati novi sadraz polja
	 */
	private String postaviText;
	/**
	 * Odredjuje rastojanje cursora od kraja, kako bi se obezbedilo da se on ne vrati na kraj 
	 */
	private int cursorOffset;
	/**
	 * Da li je strlica, home, end, kada se treba pomerati cursor
	 */
	private boolean navigtionalInput=false;
	/**
	 * Da li je pritisnuto ctrl+c
	 */
	private boolean copyInput=false;

	/**
	 * Da li sadrzaj moze biti negativan
	 */
	private boolean negativno;
	
	private DecimalFormat fullFormatter;
	
	private boolean required;

	/**
	 * Odredjuje ukupan broj karaktera koji se mogu nalaziti u polju
	 * Broj cifara, zarez, tacke, minus
	 * @param ukupanBrojCifara
	 * @param brojDecimala
	 * @return
	 */
	@SuppressWarnings("unused")
	private int ukpanBrojKaraktera(int ukupanBrojCifara, int brojDecimala){
		int cifaraPreZareza=ukupanBrojCifara-brojDecimala;
		int k=cifaraPreZareza/3;
		int o=cifaraPreZareza%3;
		int duzina;
		if (o>0)
			duzina= k + cifaraPreZareza + brojDecimala+2;
		else
			duzina= k + brojDecimala + cifaraPreZareza + 1;
		return duzina;
	}

	/**
	 * Odredjuje broj tacaka, kao separatora hiljada, koje se trebaju prikazati
	 * @param brojCifaraPreZareza
	 * @return
	 */
	public int odrediBrojTacaka(int brojCifaraPreZareza){
		int k=brojCifaraPreZareza/3;
		int o=brojCifaraPreZareza%3;
		if (o>0)
			return k;
		return k-1;
	}
	/**
	 * Odredjuje broj cifar pre prve tacke
	 * @param brojCifaraPreZareza
	 * @return
	 */
	public int odrediBrojCifaraNaPocetku(int brojCifaraPreZareza){
		int o=brojCifaraPreZareza%3;
		if (o>0)
			return o;
		return 3;
	}

	/**
	 * Odredjuje koliko se praznih mesta treba dodati do naredne tacke, nakon sto se unese broj
	 * @param text
	 * @param brojCifara
	 * @param naKraju - da li se dopunjava izmedju dva zareza ili nakon poslednjeg
	 * @return
	 */
	private String dopuni(String text,int brojCifara,boolean naKraju){
		if (brojCifara<3)
			return text;
		int o=brojCifara%3;
		if (o==0)
			return text;
		if (naKraju)
			o=brojCifaraNaPocetku-o;
		else
			o=3-o;
		String ret="";
		for (int i=0;i<o;i++)
			ret=ret.concat(" ");
		return ret.concat(text);
	}

	/**
	 * Kreira tekst kojim ce se popuniti tekstualno polje
	 * @param text
	 * @param znak koji se stavlja na povcetak - minus za negativno
	 * @return
	 */
	public String inicijalizujText(String text,Character znak){
		int indexZareza=text.indexOf(",");
		String decimale="";
		if (indexZareza>-1){
			decimale=text.substring(indexZareza,text.length());
			text=text.substring(0,indexZareza);
		}
		String ret="";
		int preostaloTacaka, brojCifara;
		if (text.equals("")){
			preostaloTacaka=brojTacaka;
			brojCifara=0;
		}	
		else{
			brojCifara=text.replace(".", "").length();
			int trenutnoTacaka=odrediBrojTacaka(brojCifara);
			preostaloTacaka=brojTacaka-trenutnoTacaka;
		}
		if (preostaloTacaka>0){
			ret+=znak;
			for (int i=0;i<brojCifaraNaPocetku;i++)
				ret=ret.concat(" ");
			for (int i=0;i<preostaloTacaka-1;i++)
				ret=ret.concat(".   ");
			ret=ret.concat(".");
			ret=ret.concat(dopuni(text,brojCifara,false));
		}
		else{ 
			ret+=znak;
			ret=ret.concat(dopuni(text,brojCifara,true));
		}
		return ret.concat(decimale);
	}

	/**
	 * Metoda se poziva kada se paste-uje.
	 * Ako se ispostvi da unos nije ispravan, vratice se se stari sadraz
	 * @param text potencijalno novi sadrzaj
	 * @return text koji ce biti prikazan nakon paste-ovanja.
	 */
	private String postaviText(String text){
		text=text.trim();
		if (text.equals(""))
			return stariText;
		int index=text.indexOf('-');
		if (index>0)//ako minus nije na pocetku nije ispravno
			return stariText;
		if (index!=-1){
			if (negativno==false)
				return stariText;
			text=text.substring(1,text.length());
			znak='-';
		}
		else  
			znak=' ';
		if ((!text.contains(",") || ispravnaPozicijaZareza(text))){
			if (brojCifaraPreZareza(text)<brojCifaraPreZareza){
				text=pripremiBroj(text);
				if (text==null)
					return stariText;
				else
					return formatiraj(text);
			}
		}
		return stariText;
	}
	

	/**
	 * 
	 * @param duzina Duzina polja, kao kod JTextField-a
	 * @param ukupanBrojCifara Ukupan broj cifara, ukljucujuci i decimale
	 * @param brojDecimala
	 * @param mozeNegativno Da li se moze upisati negativna vrednost
	 */
	public DecimalTextField (int columns,int ukupanBrojCifara, int brojDecimala, boolean mozeNegativno) {
		super(columns);
		this.setDocument(new MyDocument());
		setHorizontalAlignment(JTextField.RIGHT);
		brojCifaraPreZareza=ukupanBrojCifara-brojDecimala;
		brojTacaka=odrediBrojTacaka(brojCifaraPreZareza);
		brojCifaraNaPocetku=odrediBrojCifaraNaPocetku(brojCifaraPreZareza);
		this.brojDecimalnihMesta=brojDecimala;
		negativno=mozeNegativno;
		if (!mozeNegativno)
			znak=' ';
		setTextSuper("");
		Locale loc=new Locale(locale);
		String pattern="###.###";
		String fullPattern="###.###,";
		for (int i=0;i<brojDecimala;i++)
			fullPattern=fullPattern.concat("#");
		formatter=(DecimalFormat) DecimalFormat.getInstance(loc);
		formatter.applyLocalizedPattern(pattern);
		fullFormatter=(DecimalFormat) DecimalFormat.getInstance(loc);
		fullFormatter.applyLocalizedPattern(fullPattern);
		addFocusListener(new FocusListener() {

			/**
			 * Dopunjava do punog broja decimala - dodaje nule
			 */
			@Override
			public void focusLost(FocusEvent arg0) {
				if(!izbrisiSveSuvisneKaraktere(getTextSuper()).equals("")){
					String text=getTextSuper();
					int index=text.indexOf(',');
					if (index!=-1){
						String test=text.substring(index-1,text.length());
						if (test.startsWith("."))
							text=test.replace(".", "0");
					}
					String concat="";
					int brojDecimala=brojDecimala(text);
					if (index==-1)
						concat=concat.concat(",");
					int razlika=brojDecimalnihMesta-brojDecimala;
					for (int i=0;i<razlika;i++)
						concat=concat.concat("0");
					setTextSuper(ocisti(text.concat(concat)));
				}
				else
					setTextSuper("");
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				if (!isEditable())
					return;
				String text=getTextSuper();
				if (text.equals(""))
					text=inicijalizujText(text, znak);
				else
					text=formatiraj(getTextSuper());
				setTextSuper(text);
				setSelectionStart(getTextSuper().length());

			}
		});

		final MyDocument doc=(MyDocument) getDocument();
		/**
		 * Document listener koji sluzi kako bi se detektovalo sta se pastuje pre nego
		 * sto se promeni sadrzaj tekstualnog polja, cime se sprecava nepravilno pastovanje
		 */
		DocumentListener myDocumentListener=new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (paste)
					paste();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (paste)
					paste();
			}


			@Override
			public void changedUpdate(DocumentEvent e) {
				if (paste)
					paste();
			}

			private void paste(){
				String text=getText();
				postaviText=postaviText(text);
				postaviTextUTextFieldu(postaviText);
				paste=false;
			}
			/**
			 * Postavlja se vrednst u polju, posto to nije moguce direktno u listeneru
			 * @param text koji se postavlja
			 */
			private void postaviTextUTextFieldu(final String text){
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						setTextSuper(text);
					}
				});
			}

			/**
			 * Poziva se kada se past-uje
			 * @return sadrzaj Documenta - nije isti kao sadrzaj polja
			 */
			private String getText(){
				String text=null;
				try {
					text = doc.getText(0, doc.getLength());
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				return text;
			}
		};

		doc.addDocumentListener(myDocumentListener);

		addKeyListener(new KeyListener() {

			private boolean ignoreEvent=false, brisiSve=false;

			@Override
			public void keyTyped(KeyEvent e) {
				if (!dozvoljen(e) || ignoreEvent || brisiSve || e.getKeyChar()=='-' || e.getKeyChar()=='+')
					e.consume();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				boolean end=false;
				if (getSelectionStart()==getTextSuper().length())
					end=true;
				if (!dozvoljen(e) || ignoreEvent){
					e.consume();
					ignoreEvent=false;
				}
				else{
					if (e.getKeyChar()=='-' || e.getKeyChar()=='+'){
						setTextSuper(dodajZnak(getTextSuper(),znak));
						e.consume();
					}
					else if (brisiSve){
						setTextSuper(inicijalizujText("", znak));
						brisiSve=false;
					}
					else{
						String text=formatiraj(getTextSuper());
						if (!text.equals(getTextSuper()))
							setTextSuper(text);
					}
					if (!navigtionalInput && !end && !copyInput){
						setSelectionStart(getTextSuper().length()-cursorOffset);
						setSelectionEnd(getSelectionStart());
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				cursorOffset=getTextSuper().length()-getSelectionEnd();
				if (isPaste(e)){
					paste=true;
					ignoreEvent=true;
					setTextSuper("");
				}
				else{
					if (!dozvoljen(e)){
						e.consume();
						return;
					}
					int selectionStart=getSelectionStart();
					int selectionEnd=getSelectionEnd();
					String text=getTextSuper();
					if (e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
						if (briseNedozvoljeno(selectionStart, selectionEnd, getTextSuper()))
							e.consume();
						else if (selectionStart<=prvaPojavaCifre(text) && selectionEnd==text.length()){
							brisiSve=true;
							e.consume();
						}
						else if (!brisanjeZarezaDozvoljeno(getTextSuper(), selectionStart, selectionEnd))
							e.consume();
					}
					char c=e.getKeyChar();
					String nakonIzmene=nakonIzmene(text, selectionStart, selectionEnd,c);
					if (Character.isDigit(c)){
						if (brojCifaraPreZareza(nakonIzmene)>brojCifaraPreZareza || brojDecimala(nakonIzmene)>brojDecimalnihMesta){
							ignoreEvent=true;
							e.consume();
						}
					}	
					else if(c==','){
						if(!ispravnaPozicijaZareza(nakonIzmene)){
							ignoreEvent=true;
							e.consume();
						}
					}
					else if (c=='+' || c=='-'){
						if (c=='+')
							znak=' ';
						else 
							znak='-';
						e.consume();
					}
				}
			}	
			/**
			 * Dodaje znak + ili - na pocetak
			 * @param text
			 * @param znak
			 * @return
			 */
			private String dodajZnak(String text, char znak){
				return znak+text.substring(1,text.length());
			}
			/**
			 * Proverava da li se brise karakter koji se ne sme izbrisati, prazna mesta i tacke
			 * @param selectionStart
			 * @param selectionEnd
			 * @param text 
			 * @return
			 */
			private boolean briseNedozvoljeno(int selectionStart, int selectionEnd,String text){
				if (selectionStart==selectionEnd){
					if (selectionStart==0 || getTextSuper().charAt(selectionStart-1)==' ' || getTextSuper().charAt(selectionStart-1)=='.' || getTextSuper().charAt(selectionStart-1)=='-')
						return true;
					else 
						return false;
				}
				text=text.substring(selectionStart,selectionEnd);
				if (prvaPojavaCifre(text)!=-1)
					return false;
				else
					return true;
			}
			/**
			 * Proverava da li je pritisnut koji je dozvoljen - cifra, zarez, plus, minus, shift, strelice
			 * @param e
			 * @return
			 */
			private boolean dozvoljen(KeyEvent e){
				int i=e.getKeyCode();
				if (i==KeyEvent.VK_LEFT || i==KeyEvent.VK_RIGHT || i==KeyEvent.VK_HOME 
						|| i==KeyEvent.VK_END ||  i==KeyEvent.VK_SHIFT){
					navigtionalInput=true;
					return true;
				}
				navigtionalInput=false;
				char c=e.getKeyChar();
				if (c==KeyEvent.VK_BACK_SPACE  || Character.isDigit(c) || c==',')
					return true;
				if ((c=='+' || c=='-') &&  negativno)
					return true;
				if (e.getKeyCode() == KeyEvent.VK_C && e.getModifiers() == CTRL_MASK){
					copyInput=true;
					return true;
				}
				copyInput=false;
				return false;
			}
			/**
			 * Odredjuje da li se pastuje
			 * @param e
			 * @return
			 */
			private boolean isPaste(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_V && e.getModifiers() == CTRL_MASK){
					stariText=getTextSuper();
					setTextSuper("");
					return true;
				}
				return false;
			}

			/**
			 * Odredjuje kako bi tekst izgledao nakon izmene, uzimajuci u obzir sta je selektovano
			 * Ne vodi racuna da se postave tacke gde treba - bitan je samo broj cifara pre i posle zareza
			 * @param text
			 * @param selectionStart
			 * @param selectionEnd
			 * @param c
			 * @return
			 */
			private String nakonIzmene(String text, int selectionStart, int selectionEnd, char c){
				String prviDeo=text.substring(0,selectionStart);
				prviDeo=prviDeo+c;
				String drugiDeo="";
				if (selectionEnd<text.length()){
					drugiDeo=text.substring(selectionEnd,text.length());
				}
				return izbrisiSveSuvisneKaraktere(prviDeo.concat(drugiDeo).trim());
			}
			/**
			 * Proverava da li je dozvoljeno brisanje zareza. Posto se moze desiti  da se zarez izbrise,
			 * pa nakon toga broj cifara bude nedozovoljeno velik - broj cifara pre zareza + broj decimala 
			 * @param text
			 * @param selectionStart
			 * @param selectionEnd
			 * @return
			 */
			private boolean brisanjeZarezaDozvoljeno(String text, int selectionStart, int selectionEnd){
				String nakonBrisanja="";
				if (selectionStart==selectionEnd && selectionStart>0 && text.charAt(selectionStart-1)==',')
					nakonBrisanja=text.replace(",","");
				else if (!text.substring(selectionStart,selectionEnd).contains(","))
					return true;
				else
					nakonBrisanja=text.substring(0,selectionStart).concat(text.substring(selectionEnd,text.length()));
				if (izbrisiSveSuvisneKaraktere(nakonBrisanja).trim().length()>brojCifaraPreZareza)
					return false;
				return true;
			}

		});
	}
	/**
	 * Odredjuje koliko cifara ima pre zereza u tekstu
	 * @param text
	 * @return
	 */
	private int brojCifaraPreZareza(String text){
		int index=text.indexOf(',');
		if (index>-1)
			text=text.substring(0,index);
		text=izbrisiSveSuvisneKaraktere(text).trim();
		return text.length();
	}

	/**
	 * Brise sve karaktere koji bi mogli dovestido greke prilikom parsiranja
	 * @param text
	 * @return
	 */
	private String izbrisiSveSuvisneKaraktere(String text){
		text=text.replace("+", "");
		text=text.replace("-", "");
		text=text.replace(".", "");
		text=text.replace(" ", "");
		return text;
	}

	/**
	 * Odredjuje koliko ima decimala u tekstu
	 * @param text
	 * @return
	 */
	private int brojDecimala(String text){
		int index=text.indexOf(',');
		if (index==-1){
			return 0;
		}
		return text.substring(index+1,text.length()).trim().length();
	}	

	/**
	 * Gleda da li se zarez nalazi na dozovljenoj poziciji
	 * Ne moze se staviti tako da se time dobije vise nego sto je specificirano decimala
	 * @param text
	 * @return
	 */
	private boolean ispravnaPozicijaZareza(String text){
		text=inicijalizujText(text,znak);
		if (text.indexOf(',')!=text.lastIndexOf(','))
			return false;
		//imamo samo jedan zarez
		int index=text.indexOf(',');
		if (text.lastIndexOf('.')>index)
			return false;
		if (index>0)
			if (!Character.isDigit(text.charAt(index-1)))
				return false;
		if (index<text.length()-brojDecimalnihMesta-1)
			return false;
		return true;
	}

	/**
	 * Vrsi formatiranje teksta u saglasnosti sa zeljama veznim za buduci sadrzaj tekstualnog polja
	 * Postavljaju se tacke
	 * @param text
	 * @return
	 */
	private String formatiraj(String text){
		int cifra=prvaPojavaCifre(text);
		if (cifra==-1)
			return text;
		int index=text.lastIndexOf(" ");
		if (index>cifra)
			text=text.charAt(cifra)+text.substring(index+1);
		String formatirano=pripremiBroj(text);
		if (formatirano!=null){
			if (!formatirano.equals(""))
				return inicijalizujText(formatirano,znak);
		}
		return text;
	}

	/**
	 * Nalazi poziciju prve cifre u tekstu, vraca -1 ako nema cifara
	 * @param text
	 * @return
	 */
	private int prvaPojavaCifre(String text){
		for (int i=0;i<text.length();i++)
			if (Character.isDigit(text.charAt(i)))
				return i;
		return -1;
	}
	/**
	 * Priprema broja sdrzanog u tekstu za formatiranje
	 * @param text
	 * @return
	 */
	private String pripremiBroj(String text){
		BigDecimal dec=formatirano(text);
		if (dec==null)
			return "";
		String formatirano=formatter.format(dec);
		int index=text.indexOf(",");
		if (index>-1)
			formatirano=formatirano.concat(text.substring(index,text.length()).replace(".",""));//moze se desiti da ima vise od 3 decimalna mesta, pa zaostane tacka
		return formatirano;
	}

	private BigDecimal formatirano(String text){
		if (text==null || text.length()==0)
			return null;
		if (text.charAt(0)=='-' || text.charAt(0)=='+')
			text=text.substring(1,text.length());
		String broj=text.replace(".", "").trim(); //da ne bude exception
		if (!broj.equals("")){
			int zarez=broj.indexOf(",");
			if (zarez>-1)
				broj=broj.substring(0,zarez);
			try{
				BigDecimal dec=new BigDecimal(broj);
				return dec;
			}
			catch (Exception ex){
				return null;
			}
		}
		return null;
	}
	/**
	 * Sadrzaj se konvertuje u BigDecimal
	 * @return BigDecimal vrednost sadrzaja tekstualnog polja
	 */
	public BigDecimal getValue() {
		String text=getTextSuper().replace(" ","");
		text=text.replace(".", "");;
		text=text.replace(",", ".");
		try{
			return new BigDecimal(text);
		}
		catch(Exception ex){
			return null;
		}
	}

	
	private String ocisti(String text){
		int poz=prvaPojavaCifre(text);
		if (poz==-1)
			return "";
		text=znak+text.substring(poz,text.length());
		return text;
	}
	
	public void setTextSuper(String text){
		super.setText(text);
	}

	public String getTextSuper(){
		return super.getText();
	}

	/**
	 * Vraca tekst ne kao sto je u polju, nego kao sto izgleda BigDecimal
	 * @return
	 */
	@Override  
	public String getText() {
		String text=super.getText();
		text=pripremiBroj(text);
		text=text.replace(".", "");
		text=text.replace(",", ".");
		if (znak=='-')
			text=znak+text;
		System.out.println(text);
		return text;
	}

	@Override
	public void setText(String text){
		if (text.equals("")){
			znak=' ';
			super.setText(inicijalizujText(text,znak));
		}
		text=text.replace('.',',').trim();
		if (text.startsWith("-")){
			znak='-';
			text=text.substring(1,text.length());
		}
		znak=' ';
		text=formatiraj(text);
		super.setText(ocisti(text));
	}
	/**
	 * Postavlja sadrzaj polja na osnovu BidDecimal vrednosti
	 * @param iznos
	 */
	public void setValue(BigDecimal iznos) throws NegativeValueException{
		String text=iznos.toString();
		text=fullFormatter.format(iznos);

		if (iznos.compareTo(BigDecimal.valueOf(0))==-1){
			if (negativno==false)
				throw new NegativeValueException("Vrednost ne sme biti negativna");
			znak='-';
		}
		else
			znak=' ';
		String concat="";
		int brojDecimala=brojDecimala(text);
		if (!text.contains(","))
			concat=concat.concat(",");
		int razlika=brojDecimalnihMesta-brojDecimala;
		for (int i=0;i<razlika;i++)
			concat=concat.concat("0");
		text=text.concat(concat);
		setTextSuper(text);
	}

	public boolean isEmpty(){
		return izbrisiSveSuvisneKaraktere(getTextSuper()).trim().equals("");
	}

	/**
	 * Eliminise problem koji se moze javiti kada se koristi DocumentListener
	 * DocumentListener moze u nekim slucajevima registuje dva dogadjaja, a radi se o samo jednom
	 */
	private class MyDocument extends PlainDocument {
		private static final long serialVersionUID = 1L;

		private boolean ignoreEvents = false;

		@Override
		public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			String oldValue = DecimalTextField.this.getTextSuper();
			this.ignoreEvents = true;
			super.replace(offset, length, text, attrs);
			this.ignoreEvents = false;
			String newValue = DecimalTextField.this.getTextSuper();
			if (!oldValue.equals(newValue)) DecimalTextField.this.firePropertyChange(TEXT_PROPERTY, oldValue, newValue);
		}

		@Override
		public void remove(int offs, int len) throws BadLocationException {
			String oldValue = DecimalTextField.this.getTextSuper();
			super.remove(offs, len);
			String newValue = DecimalTextField.this.getTextSuper();
			if (!ignoreEvents && !oldValue.equals(newValue)) DecimalTextField.this.firePropertyChange(TEXT_PROPERTY, oldValue, newValue);
		}
	}

	@Override
	public boolean isEditValid() {
		if (required)
			return !isEmpty();
		return true;
	}
}