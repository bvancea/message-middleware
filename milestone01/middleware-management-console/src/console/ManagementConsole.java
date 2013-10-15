package console;

import java.util.Scanner;

public class ManagementConsole {
	
	private CommandExecutor executor;

	public ManagementConsole() {
		executor = new CommandExecutor();
	}

	public void acceptInput() {
		
		showCommands();
		
		Scanner in = new Scanner(System.in);
		System.out.print(">>");
		
		boolean running = true;
		while (running) {
			if (in.hasNext()) {
				String command = in.next();
				
				//System.out.println(command);
				
				if (command.trim().equals("exit")) {
					in.close();
					running = false;
				} else if (command.trim().equals("show-queues")) {
					System.out.println("Displaying all queues in the system...");
					executor.showQueues();
				} else if (command.trim().startsWith("show-messages")) {
//					System.out.println("Executing show-messages");
					if (in.hasNextInt()) {
						int queueId = in.nextInt();
						System.out.println("Displaying messages in queue " + queueId + "...");
						executor.showMessages(queueId);
					} else if (in.hasNext()) {
						String arg = in.next();
						if (arg.trim().equals("-all")) {
							System.out.println("Displaying all messages in the system...");
							executor.showAllMessages();
						} else {
							System.out.println("Invalid command arguments.");
						}	
					}
				} else if (command.trim().equals("show-clients")) {
					System.out.println("Displaying all registered clients...");
					executor.showClients();
				} else if (command.trim().equals("help")) {
					showCommands();
				} else {
					System.out.println("Unknown command.");
				}
				
				if (running) System.out.print(">>");
			}
		}

	}
	
	private void showCommands() {
		System.out.println("Available commands:");
		System.out.println("show-queues - displays state of all queues");
		System.out.println("show-messages <queue_id> - displays all messages in queue <queue_id>");
		System.out.println("show-messages -all - displays all messages in all queues");
		System.out.println("show-clients - displays all registered clients");
		System.out.println("help - display help");
		System.out.println("exit - stop execution");
	}

}
