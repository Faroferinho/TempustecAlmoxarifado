package functions;

import javax.swing.JOptionPane;

import main.Almoxarifado;

public class Login {
	
	public Login() {
		
		boolean auxLogin = false;
		if(Almoxarifado.state == 0) {
			String acount = "";
			String password = "";
			
			do{
				System.out.println("Conta: " + acount);
				acount += JOptionPane.showInputDialog("Preencha seu Registro");
			}while(acount.isEmpty() || acount.isBlank() || acount.equals("null"));
			
			password += JOptionPane.showInputDialog("Insira sua senha");
			if(password.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada");
				System.exit(0);
			}
			
			entering(acount, password, auxLogin);
		}
	}
	
	public static void entering(String acount, String password, boolean auxLogin) {
		
		
		String accnts[] = new String[Almoxarifado.quantityWorkers];
		String psswrds[] = new String[Almoxarifado.quantityWorkers];
		
		String toSplit = "";
		toSplit += DBConector.readDB("RdF", "funcionarios", 2);
		accnts = toSplit.split("\n");
		
		
		toSplit = "";
		toSplit += DBConector.readDB("password", "funcionarios", 2);
		psswrds = toSplit.split("\n");
		
		for(int i = 0; i < Almoxarifado.quantityWorkers; i++) {
			//System.out.println("Conta Escrita: " + acount + ", Conta no Index " + (i + 1) + ": " + accnts[i]);
			if(acount.equals(accnts[i]) && password.equals(psswrds[i])) {
				System.out.println("Conta e Senha Escritas batem com a Conta e Respectiva Senha no DataBase, RdF: " + accnts[i] +  " Senha: " + psswrds[i]);
				auxLogin = true;
			}
		}
		if(auxLogin) {
			
			Almoxarifado.name = DBConector.findInDB("name", "funcionarios", "RdF", acount);
			//System.out.println("O nome é: " + Almoxarifado.name);
			Almoxarifado.rdf = acount;
			System.out.println("O RdF é: " + Almoxarifado.rdf);
			Almoxarifado.cpf = DBConector.findInDB("cpf", "funcionarios", "RdF", acount);
			System.out.println("O CPF é: " + Almoxarifado.cpf);
			Almoxarifado.type = DBConector.findInDB("type", "funcionarios", "RdF", acount);
			System.out.println("O tipo é: " + Almoxarifado.type);
			Almoxarifado.state = 1;
		}else {
			System.exit(0);
		}
	}
}
