import Firm.Task;
import Firm.RoundOperator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MultiServer
{
    private ServerSocket serverSocket;
    private static RoundOperator ro = new RoundOperator();
    private static String roJSON;
    private final static String filePath = "info.txt";

    public void start(int port) throws IOException
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            roJSON = readFile(filePath, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        ro = gson.fromJson(roJSON, RoundOperator.class);

//        ro.addTask("Первый раунд", new Task("На движение", "s=vt", 1,
//                "120", "00:05", 10, 0,
//                "Ваня ехал на велосипеде со скоростью 20км/ч 2 часа, какое расстояние он проехал?"));
//        ro.addTask("Первый раунд", new Task("На проценты", "a*(p/100)=b", 2,
//                "120", "00:10", 15, 0,
//                "Маша купила 14кг яблок, это 10% запаса яблок в магазине, сколько яблок в магазине?"));
//
//        ro.addTask("Второй раунд", new Task("На движение", "s=vt", 1,
//                "100", "00:15", 20, 0,
//                "Витя проехал 39 км. Сколько времени он был в пути, если его скорость – 13 км/ч?"));
//        ro.addTask("Второй раунд", new Task("На логику", "В комплекте", 2,
//                "80", "00:20", 25, 1,
//                "Что не вместится даже в самую большую кастрюлю?"));
//
//        roJSON = gson.toJson(ro);
//        writeFile(filePath, roJSON);

        serverSocket = new ServerSocket(port);
        while (true)
        {
            new EchoClientHandler(serverSocket.accept()).start();
        }
    }

    public void stop() throws IOException
    {
        serverSocket.close();
    }

    private static class EchoClientHandler extends Thread
    {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run()
        {
            try
            {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            String inputLine = null;
            while (true)
            {
                try
                {
                    if ((inputLine = in.readLine()) == null) break;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                if (".".equals(inputLine))
                {
                    out.println("bye");
                    break;
                }
                if ("REFRESH".equals(inputLine))
                {
                    out.println(roJSON);
                }
                if (inputLine != null)
                {
                    if ('d' == inputLine.charAt(0))     // d0,1
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String[] ids = inputLine.substring(1).split(",");
                        int groupID = Integer.parseInt(ids[0]);
                        int examID = Integer.parseInt(ids[1]);
                        ro.delTask(groupID, examID);
                        roJSON = gson.toJson(ro);
                        writeFile(filePath, roJSON);
                        out.println(roJSON);
                    }
                    if ('e' == inputLine.charAt(0))     // e0,3##json
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String[] parts = inputLine.substring(1).split("##");
                        String[] ids = parts[0].split(",");
                        int groupID = Integer.parseInt(ids[0]);
                        int examID = Integer.parseInt(ids[1]);
                        Task tempTask = gson.fromJson(parts[1], Task.class);
                        ro.editTask(groupID, examID, tempTask);
                        roJSON = gson.toJson(ro);
                        writeFile(filePath, roJSON);
                        out.println(roJSON);
                    }
                    if ('u' == inputLine.charAt(0))     // ujson
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        RoundOperator tempGo = gson.fromJson(inputLine.substring(1), RoundOperator.class);
                        ro.setRounds(tempGo.getRounds());
                        roJSON = gson.toJson(ro);
                        writeFile(filePath, roJSON);
                    }
                    if ('a' == inputLine.charAt(0))
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();        // agroupName##json
                        Gson gson = gsonBuilder.create();
                        String[] parts = inputLine.substring(1).split("##");
                        Task tempTask = gson.fromJson(parts[1], Task.class);
                        ro.addTask(parts[0], tempTask);
                        roJSON = gson.toJson(ro);
                        writeFile(filePath, roJSON);
                        out.println(roJSON);
                    }
                }
            }

            try
            {
                in.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            out.close();
            try
            {
                clientSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static String readFile(String path, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void writeFile(String path, String text)
    {
        try(FileWriter writer = new FileWriter(path, false))
        {
            writer.write(text);
            writer.flush();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
