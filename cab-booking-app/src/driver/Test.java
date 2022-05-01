package driver;

import commands.CommandExecutor;
import commands.CommandExecutorFactory;
import model.Command;
import service.CabBookingService;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        CabBookingService service = new CabBookingService();

        while (true) {
            System.out.println("Enter the command");
            String inputLine = scanner.nextLine();
            Command command = new Command(inputLine);
            if (command.getCommand().equals("exit")) {
                break;
            }

            CommandExecutor commandExecutor = new CommandExecutorFactory(service).getCommandExecutor(command);
            if (commandExecutor.validate(command)) {
                commandExecutor.execute(command);
            }

        }

    }
}
