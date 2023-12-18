package functions;

import javax.swing.JOptionPane;

import main.Almoxarifado;

public class Login {
	
	public Login() {
		
		boolean auxLogin = false;
		if(Almoxarifado.state == 0) {
			String acount = "";
			String password = "";
			
			acount += JOptionPane.showInputDialog(null, "Preencha seu Registro", "Entrada", JOptionPane.PLAIN_MESSAGE);
			if(acount.equals("") || acount.equals("null")) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
			
			password += JOptionPane.showInputDialog(null, "Insira sua senha", "Senha", JOptionPane.PLAIN_MESSAGE);
			if(password.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
			entering(acount, password, auxLogin);
		}
	}
	
	public static void entering(String acount, String password, boolean auxLogin) {
		
		
		String accnts[] = new String[Almoxarifado.quantityWorkers];
		String psswrds[] = new String[Almoxarifado.quantityWorkers];
		
		String toSplit = "";

		toSplit += DBConector.readDB("CPF", "funcionarios");
		accnts = toSplit.split("\n");
		
		
		toSplit = "";
		toSplit += DBConector.readDB("password", "funcionarios");
		psswrds = toSplit.split("\n");
		
		for(int i = 0; i < Almoxarifado.quantityWorkers; i++) {
			//System.out.println("Conta Escrita: " + acount + ", Conta no Index " + (i + 1) + ": " + accnts[i]);
			if(acount.equals(accnts[i]) && password.equals(psswrds[i])) {
				System.out.println("Conta e Senha Escritas batem com a Conta e Respectiva Senha no DataBase, RdF: " + accnts[i] +  " Senha: " + psswrds[i]);
				auxLogin = true;
			}
		}
		if(auxLogin) {

			Almoxarifado.name = "";
			Almoxarifado.name += DBConector.findInDB("name", "funcionarios", "CPF", acount);
			System.out.println("O nome é: " + Almoxarifado.name);
			
			Almoxarifado.rdf = "";
			Almoxarifado.rdf += DBConector.findInDB("RdF", "funcionarios", "CPF", acount);
			System.out.println("O RdF é: " + Almoxarifado.rdf);
			
			Almoxarifado.cpf = acount;
			System.out.println("O CPF é: " + Almoxarifado.cpf);
			
			Almoxarifado.rdf = "";
			Almoxarifado.type += DBConector.findInDB("type", "funcionarios", "CPF", acount);
			System.out.println("O tipo é: " + Almoxarifado.type);
			
			Almoxarifado.state = 1;
		}else {
			JOptionPane.showMessageDialog(null, "Conta ou Senha Incorretos", "Erro no Login", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
}
