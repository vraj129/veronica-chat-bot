/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package veronica;

/**
 *
 * @author Vraj Patel
 */
//rmi://localhost:1991/veronica
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.Random;
public class VeronicaServer extends UnicastRemoteObject implements veronica_interface {

    public VeronicaServer() throws RemoteException
    {
        
    }
    @Override
    public String user_chat(String message) throws RemoteException {
        //Server Utility
        try
        {
            String[] answer = new String[10];
            int i = 0;
            //Database Connectivity
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/veronica","root","");
             Statement stm = con.createStatement();
            String sql = "select * from veronica_question_bank where question='"+message+"'";//User text finding query 
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next())
            {
                answer[i] = rs.getString("answer");
                //System.out.println(answer[i]);
                i++;
            }
           
            System.out.println("i : "+i);
            if(i>0)
            {
                Random random = new Random();
                int rand = random.nextInt(i+0)+0;//randomizing the answer
                //con.close();
                return answer[rand];
            }
            else
            {
                
                //String str = "What are you doing today?";
                String split[] = message.split(" ", 0);//split the message 
                for(String word:split)
                {
                    String[] split_answer = new String[20];
                    int j = 0;
                    Statement stm_1 = con.createStatement();
                    System.out.println("Word : "+word);
                    String sql1 = "select * from veronica_question_bank where question='"+word+"'";//seleting the word 
                    ResultSet rs_1 = stm_1.executeQuery(sql1);
                    while(rs_1.next())
                    {
                        split_answer[j]=rs_1.getString("answer");//Storing it in the array
                        j++;
                    }
                    System.out.println("j : "+j);
                    if(j>0)
                    {
                        Random random = new Random();
                        int rand = random.nextInt(j+0)+0;//Randomizing it...
                        return split_answer[rand];
                    }
                    //con.close();
                    
                }    
                    
                        //con.close();
                        int rand = (int)(Math.random() * 3+1);
                        if(rand == 1)//If no answer is found
                        {
                            return "I don't Understand";
                        }
                        else if(rand == 2)
                        {
                            return "Sorry I didn't get you....";
                        }
                        else if(rand == 3)
                        {
                            return "Did not get you...";
                        }
                    
                
                /*int rand = (int)(Math.random() * 3+1);
                if(rand == 1)
                {
                    return "I don't Understand";
                }
                else if(rand == 2)
                {
                    return "Sorry I didn't get you....";
                }
                else if(rand == 3)
                {
                    return "Did not get you...";
                }*/
            }
            
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
   
    public static void main(String[] args) throws RemoteException
    {
        try
        {
            //Server Networking with RMI
            Registry reg = LocateRegistry.createRegistry(1991);
            reg.rebind("Veronica server",new VeronicaServer());//Skeleton
            System.out.println("Server is Ready !!!");
        }
        catch(RemoteException e)
        {
            System.out.println("exception"+e);
        }
    }
    
}
