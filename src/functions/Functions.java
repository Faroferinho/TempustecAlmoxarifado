package functions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import main.Almoxarifado;

public class Functions {

	public Functions() {
		// TODO Auto-generated constructor stub
	}
	
	public static ArrayList<String> listToArrayList(String list[]) {
		ArrayList<String> toReturn = new ArrayList<>();
		
		List<String> auxList = Arrays.asList(list);
		
		toReturn = new ArrayList<>(auxList);
		
		return toReturn;
	}
	
	public static String randomGreetingsGen() {
		String greeting = "";
		Random rand = new Random();
		int time = LocalDateTime.now().getHour();
		
		if(time > 11 && time < 18) {
			greeting = "Boa Tarde";
		}else {
			greeting = "Bom Dia";
		}
		
		switch(rand.nextInt(11)) {
		case 1:
			greeting += ", Como tem passado?";
			break;
		case 2:
			greeting += ", Espero que este e-mail te encontre bem!";
			break;
		case 3:
			greeting += " Estimado/a.";
			break;
		case 4:
			greeting += " e Saudações.";
			break;
		case 5:
			greeting += " Cara Equipe.";
			break;
		case 6:
			greeting += " Caros Colegas.";
			break;
		case 7: 
			greeting += " a Toda Equipe.";
			break;
		case 8:
			greeting += " e Saudações a Todos";
			break;
		case 9:
			greeting += " a todos.";
			break;
		case 10:
			greeting += " Estimada Equipe";
			break;
		default:
			greeting += ".";
			break;
		}
		
		return greeting;
	}

	public static boolean emptyString(String s) {
		if(s.equals("") || s.equals(null) || s.equals("null")) {
			System.out.println("é nula");
			return true;
		}
		System.out.println("não é nula");
		return false;
	}
	
	public static boolean isOnBox(int x, int y, int w, int h) {
		if(Almoxarifado.mX > x && Almoxarifado.mX < x + w) {
			if(Almoxarifado.mY > y && Almoxarifado.mY < y + h) {
				return true;
			}
		}
		return false;
	}
	
	public static double diferenceCurency(String v1, String v2) {
		double returnValue = 0;
		
		String values1[] = v1.split("[.]");
		String values2[] = v2.split("[.]");
		System.out.println(values1[0]);
		int before1 = Integer.parseInt(values1[0]);
		System.out.println(values2[0]);
		int before2 = Integer.parseInt(values2[0]);
		System.out.println(values1[1]);
		int after1 = Integer.parseInt(values1[1]);
		System.out.println(values2[1]);
		int after2 = Integer.parseInt(values2[1]);
		
		returnValue += before1 - before2;
		returnValue += (after1 - after2) * 0.01;
		
		return returnValue;
	}
	
}
