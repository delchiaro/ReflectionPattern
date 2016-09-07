package reflection.IO;

import reflection.model.knowledge.FactType;
import reflection.model.knowledge.quantity.Unit;
import reflection.model.operational.Fact;

import java.io.IOException;
import java.util.*;

/**
 * Created by nagash on 03/09/16.
 */
public class Manager {

    List<Unit> units = new ArrayList<>();
    List<FactType> factTypes = new ArrayList<>();
    List<Fact>     facts = new ArrayList<>();


    public static void main(String... args)
    {
        Manager man = new Manager();
        try {
            man.mainMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void mainLoop() throws IOException {
        while(true)
        {
            mainMenu();

        }
    }


    private final static int STAR = 5;

    public void mainMenu() throws IOException {
        while(true) {
            int choice = menu(STAR, "Reflection Manager: Main menu",
                    "Manage Knowledge   level",
                    "Manage Operational level");
            switch (choice) {
                case 0:
                    return;
                case 1:
                    knowledgeMainMenu();
                case 2:
                    break;
            }
        }
    }

    public void knowledgeMainMenu() throws IOException {
        while(true) {
            int choice = menu(STAR, "Reflection Manager: Knowledge - Main menu",
                    "Manage Measurement Units",
                    "Manage Fact Types");

            switch (choice) {
                case 0: return;
                case 1: knowledgeUnitMenu(); break;
                case 2:                      break;
            }
        }
    }

    public void knowledgeUnitMenu() throws IOException {
        while(true)
        {
            int choice = menu(STAR, "Reflection Manager: Knowledge - Measurement Units menu",
                    "List units",
                    "Add Unit",
                    "Manage Unit");

            switch (choice) {
                case 0: return;
                case 1:  listUnits(); break;
                case 2: addUnit(); break;
                case 3:  listUnits(); int number = intInput("Select unit number: "); knowledgeUnitDetailMenu(this.units.get(number-11)); break;
            }
        }
    }
    public void listUnits() {
        int i=1;
        for(Unit unit : this.units)
            println((i++) + ")" + unit.getSymbol() + "\t-\t" + unit.getName());

    }
    public void addUnit() {
        String name = input("New Measurement Unit name: ");
        String sym = input("New Measurement Unit symbol: ");
        this.units.add(new Unit(name, sym));
    }



    public void knowledgeUnitDetailMenu(Unit unit) throws IOException {
        while(true)
        {
            int choice = menu(STAR, "Reflection Manager: Knowledge - Measurement Units Details - " + unit.getName(),
                    "Add Conversion",
                    "Remove Unit");
            listConversion(unit);
            switch (choice) {
                case 0:
                    return;
                case 1:  listUnits(); break;
                case 2: units.remove(unit); break;
            }
        }
    }

    public void listConversion(Unit unit) {
        Map<Unit, Number> conversions = unit.getConversions();
        int i = 1;
        for( Map.Entry<Unit, Number> conv : conversions.entrySet())
            println((i++) + ")" + unit.getSymbol() + "\t = \t" + conv.getValue().doubleValue() + "  " + conv.getKey().getSymbol() );

    }




    private static final int menu(int stars, String menuName, String... menuOptions) throws IOException {
        String str = " ";
        for(int i=0; i<stars; i++)
            str = str + "* ";

        return menu(str+menuName+str, menuOptions );
    }
    private static final int menu(String menuName, String... menuOptions ) throws IOException {
        clear();
        println(menuName);
        println("");

        for(int i=0; i<menuOptions.length; i++)
        {
            println((i+1) + ") " + menuOptions[i]);
        }
        println("0) Back");
        print("> ");
        Scanner scanner = new Scanner(System.in);

        int choice = scanner.nextInt();
        return choice;
    }
    private static final void clear() {
        try {
            Runtime.getRuntime().exec("clear");
        } catch (IOException e) {
            e.printStackTrace();
            println("Clear goes wrong..");
        }
    }
    private static final void print(String str) {
        System.out.print(str);
    }
    private static final void println(String str) {
        System.out.print(str + "\n");
    }
    private static final String input(String descr) {
        println(descr);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    private static final int intInput(String descr) {
        println(descr);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

}
