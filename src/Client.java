
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Client {

    Socket ClientSocket;
    String serverIP = "127.168.0.1"; //localHost
    int Port = 2400;
    String coeff[] = new String[3];
    String va[] = new String[3];

    BufferedReader fromServer;
    DataOutputStream outToServer;
    String sCoeff;
    public Client() throws IOException {

        ClientSocket = new Socket(serverIP, Port);
        System.out.println("Connecting to server IP:" + serverIP);

        fromServer = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
        outToServer = new DataOutputStream(ClientSocket.getOutputStream());
        while (true) {

        	sCoeff = fromServer.readLine();

            if (sCoeff.equals("Enter equation coefficients or E to exit")) {

                Scanner s = new Scanner(System.in);

                System.out.println("Enter 1st Coefficient");
                coeff[0] = s.next();

                if (coeff[0].equalsIgnoreCase("E")) {
                    outToServer.writeBytes("E" + "\n");
                    System.out.println("Ecpires");
                    break;
                } else {

                    System.out.println("Enter 2nd Coefficient");
                    coeff[1] = s.next();

                    if (coeff[1].equalsIgnoreCase("E")) {
                        outToServer.writeBytes("E" + "/n");
                        System.out.println("Ecpires");
                        break;
                    } else {

                        System.out.println("Enter 3rd Coefficient");
                        coeff[2] = s.next();

                        if (coeff[2].equalsIgnoreCase("E")) {
                            outToServer.writeBytes("E" + "/n");
                            System.out.println("Ecpires");
                            break;
                        } else {

                            outToServer.writeBytes(coeff[0] + " " + coeff[1] + " " + coeff[2] + " " + "\n");

                            String serverResult = fromServer.readLine();

                            if (serverResult.equals("Please Enter Equation result")) {
                                System.out.println("Enter The Rusult");
                                String result = s.next();

                                if (result.equalsIgnoreCase("E")) {

                                    outToServer.writeBytes("E" + "\n");
                                    System.out.println("Expired");
                                    break;
                                } else {

                                    long starttime = System.currentTimeMillis();

                                    outToServer.writeBytes(result + "\n");

                                    String variable = fromServer.readLine();

                                    long endtime = System.currentTimeMillis();

                                    long RTT = endtime - starttime;

                                    System.out.println("RTT = " + RTT + "ms");

                                    if (variable.equalsIgnoreCase("no answer")) {
                                        System.out.println("No Answer");

                                    } else {

                                        StringTokenizer stk = new StringTokenizer(variable, " ");
                                        int vaNum = 0;
                                        while (stk.hasMoreTokens()) {

                                            va[vaNum] = stk.nextToken();
                                            vaNum++;
                                        }
                                        
    										System.out.println("equation="+ coeff[0]+ ")"+va[0]
    												+ " + ( " + coeff[1]+ ")*"+ va[1]
    														+ " + ( " + coeff[2]+ ")*"+ va[2]
    																+ "=" + result+ "\r"
    												);
    										 System.out.println("coefficient P1 :"+coeff[0]+ "\r"+
    	    											"coefficient P2 :"+coeff[1]+ "\r"+
    	    												"coefficient P3 :"+coeff[2]+ "\r"
    	    												+ " variable x ="+va[0]+"r"
    	    												+ " variable y ="+va[1]+"\r"
    	    												+ " variable z ="+va[2]+"\r"
    	    												+ " result ="+result+"\r"
    	    												);
                                    }
                                }
                            }

                        }
                    }
                }

            }

        }

    }

    public static void main(String args[]) throws IOException {

        Client c = new Client();

    }

}
