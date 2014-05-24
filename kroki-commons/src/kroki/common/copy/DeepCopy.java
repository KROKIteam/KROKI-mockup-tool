/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.common.copy;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Utility for making deep copies (vs. clone()'s shallow copies) of
 * objects. Objects are first serialized and then deserialized. Error
 * checking is fairly minimal in this implementation. If an object is
 * encountered that cannot be serialized (or that references an object
 * that cannot be serialized) an error is printed to System.err and
 * null is returned. Depending on your specific application, it might
 * make more sense to have copy(...) re-throw the exception.
 */
public class DeepCopy {

	/**
	 * Returns a copy of the object, or null if the object cannot
	 * be serialized.
	 */
	public static Object copy(Object orig) {
		Object obj = null;
		try {
			// Write the object out to a byte array
			FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(fbos);
			out.writeObject(orig);
			out.flush();
			out.close();

			// Retrieve an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in =
					new ObjectInputStream(fbos.getInputStream());
			obj = in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return obj;
	}

	public static void save(Object orig, File file) {
		try {
			// Write the object out to a byte array
			FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(fbos);
			out.writeObject(orig);
			out.flush();
			out.close();

			FileOutputStream fOut = new FileOutputStream(file);
			fOut.write(fbos.getByteArray());
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean saveXStream(Object toSave, File file){
		try{
			XStream xstream = new XStream(new DomDriver());
			BufferedWriter  out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			xstream.toXML(toSave, out);
			out.close();

		} catch (Exception e) { 
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static Object open(File file) {
		Object obj = null;
		try {
			InputStream ifile = new FileInputStream(file);
			InputStream buffer = new BufferedInputStream(ifile);
			ObjectInput input = new ObjectInputStream(buffer);
			obj = input.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return obj;
	}



	public static Object loadXStream(File file) {

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			XStream xstream = new XStream();
			//configureAliases();
			return xstream.fromXML(in);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
