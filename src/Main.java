import com.sistema.servidor.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ChatServer server = new ChatServer();

        try {
            server.startServer();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar o servidor: " + ex);
        }

        System.out.println("Servidor Finalizado!");
    }
}
