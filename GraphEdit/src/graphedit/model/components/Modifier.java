package graphedit.model.components;

public enum Modifier {
	PRIVATE {
		public String toString() { return "-"; }
	}, 
	PROTECTED {
		public String toString() { return "#"; }
	}, 
	DEFAULT {
		public String toString() { return "~"; }
	},  
	PUBLIC {
		public String toString() { return "+"; }
	}
}
