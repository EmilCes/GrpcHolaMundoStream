package grpcholamundostream.servidor;

import java.util.Scanner;
import com.proto.saludo.Holamundo.SaludoRequest;
import com.proto.saludo.Holamundo.SaludoResponse;
import com.proto.saludo.SaludoServiceGrpc.SaludoServiceImplBase;
import io.grpc.stub.StreamObserver;;

public class ServidorImpl extends SaludoServiceImplBase {

    @Override
    public void saludo(SaludoRequest request, StreamObserver<SaludoResponse> responseObserver) {
        SaludoResponse respuesta = SaludoResponse.newBuilder().setResultado("Hola " + request.getNombre()).build();
        responseObserver.onNext(respuesta);
        responseObserver.onCompleted();
    }

    @Override
    public void saludoStream(SaludoRequest request, StreamObserver<SaludoResponse> responseObserver) {
        // Es la misma función de arriba, solo con este for
        // para simular el envío de multiples chunks de datos
        for (int i = 0; i <= 10; i++) {
            // Se construye la respuesta a enviarle al cliente
            SaludoResponse respuesta = SaludoResponse.newBuilder()
                    .setResultado("Hola " + request.getNombre() + " por " + i + " vez.")
                    .build();
            
            // En gRPC se utiliza onNext para enviar la respuesta
            // En llamadas unarias, solo se llama una vez
            responseObserver.onNext(respuesta);
        }

        // Avisa que se ha terminado
        responseObserver.onCompleted();
    }

    @Override
    public void saludoArchivoStream(SaludoRequest request, StreamObserver<SaludoResponse> responseObserver) {
        String archivoNombre = "/" + request.getNombre();

        System.out.println("Inicio de Streaming");

        try (Scanner scanner = new Scanner(ServidorImpl.class.getResourceAsStream(archivoNombre))) {
            while (scanner.hasNextLine()) {
                SaludoResponse respuesta = SaludoResponse
                        .newBuilder()
                        .setResultado(scanner.nextLine())
                        .build();

                responseObserver.onNext(respuesta);
                System.out.print(".");
            }

            System.out.println("Envío de Streaming Terminado");
            responseObserver.onCompleted();
        }
    }
}


