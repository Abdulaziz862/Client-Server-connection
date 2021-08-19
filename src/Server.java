
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

	int port = 2400;

	ServerSocket welcome;

	public Server() throws IOException {

		System.out.println("server is wating for a new connection");

		welcome = new ServerSocket(port);//open socket to the given port

		while (true) {//server is accepting a new connection

			Runnable runnable = new Runnable() {// interface that is to be implemented by a class whose instances are intended to be executed by a thread. 
				public void run() {

					try {
						Socket clientsocket = welcome.accept();
						System.out.println("server is accepting a new connection");

						String connection = "";
						int coeff[] = new int[3];
						int noAnswer[] = new int[1];

						int cieffNum;
						int eqresult;
						BufferedReader Clientpayload;
						DataOutputStream Toclient;

						Clientpayload = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));

						Toclient = new DataOutputStream(clientsocket.getOutputStream());

						while (!connection.equals("exit")) {// as long as  the client dont exits the connection the loop keeps running

							connection = "";

							Toclient.writeBytes("Enter equation coefficients or E to exit" + "\n");

							String coefficients = Clientpayload.readLine();

							StringTokenizer k = new StringTokenizer(coefficients, " ");//a class allows you to break a string into tokens

							cieffNum = 0;

							while (k.hasMoreTokens()) {//checks if there is more tokens available

								String P = k.nextToken();//returns the next token from the StringTokenizer object

								if (!P.equalsIgnoreCase("E")) {
									coeff[cieffNum] = Integer.parseInt(P);
									cieffNum++;

								} else {
									System.out.println("client socket exit");
									clientsocket.close();
									connection = "exit";

								}

							}

							if (!connection.equals("exit")) {

								Toclient.writeBytes("Please Enter Equation result" + "\n");//It is used to write string to the output stream as a sequence of bytes


								String equationresult = Clientpayload.readLine();//It is used for reading a line of text


								if (!equationresult.equals("") && !equationresult.equalsIgnoreCase("E")) {
									eqresult = Integer.parseInt(equationresult);
									ArrayList<int[]> arrayList = new ArrayList();
									arrayList = SolveEq(coeff, eqresult);

									noAnswer = arrayList.get(0);
									int v[] = arrayList.get(1);

									if (noAnswer[0] == -1) {
										if (connection != "exit") {
											Toclient.writeBytes("no answer" + "\n");
										}

									} else {
										if (connection != "exit") {
											Toclient.writeBytes(v[0] + " " + v[1] + " " + v[2] + " " + "\n");
										}

									}

								} else {
									clientsocket.close();
									connection = "exit";

								}

							}

						}
					} catch (IOException ex) {
						Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
					}

				}

			};

			Thread thread = new Thread(runnable);
			thread.start();
		}
	}

	int n = 100;

	public ArrayList<int[]> SolveEq(int coeff[], int result) {

		ArrayList List = new ArrayList();
		int noAnswer[] = new int[1];
		int p1 = coeff[0];
		int p2 = coeff[1];
		int p3 = coeff[2];

		int EquationVariable[] = new int[3];

		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				for (int z = 0; z < n; z++) {
					int calc = p1 * x + p2 * y + p3 * z;
					if (calc == result) {
						EquationVariable[0] = x;
						EquationVariable[1] = y;
						EquationVariable[2] = z;
						noAnswer[0] = 0;

						List.add(noAnswer);
						List.add(EquationVariable);

						return List;
					}
				}
			}
		}

		noAnswer[0] = -1;
		List.add(EquationVariable);
		List.add(noAnswer);
		return List;
	}

	public static void main(String args[]) throws IOException {

		Server s = new Server();

	}

}
