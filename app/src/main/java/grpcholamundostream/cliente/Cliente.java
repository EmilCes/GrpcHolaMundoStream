package grpcholamundostream.cliente;

import com.proto.saludo.SaludoServiceGrpc;
import com.proto.saludo.Holamundo.SaludoRequest;
import com.proto.saludo.Holamundo.SaludoResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Cliente {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 9090;

        ManagedChannel ch = ManagedChannelBuilder
                .forAddress(host, puerto)
                .usePlaintext()
                .build();
        
        //saludarUnario(ch);

        //saludarStream(ch);

        saludarArchivoStream(ch);

        System.out.println("Apagando...");
        ch.shutdown();
    }

    // Saludo una vez (unario)
    public static void saludarUnario(ManagedChannel ch) {
        // Obtenemos referencia al stub
        SaludoServiceGrpc.SaludoServiceBlockingStub stub = SaludoServiceGrpc.newBlockingStub(ch);

        // Construimos la petición enviando un parametro
        SaludoRequest peticion = SaludoRequest.newBuilder().setNombre("César").build();

        // Usando el stub, realizamos la llamada RPC
        SaludoResponse respuesta = stub.saludo(peticion);

        // Imprimimios la respuesta de RPC
        System.out.println("Respuesta RPC: " + respuesta.getResultado());
    }

    public static void saludarStream(ManagedChannel ch) {
        // Obtenemos referencia al stub
        SaludoServiceGrpc.SaludoServiceBlockingStub stub = SaludoServiceGrpc.newBlockingStub(ch);

        // Construimos la petición enviando un parametro
        SaludoRequest peticion = SaludoRequest.newBuilder().setNombre("César").build();

        // Usando el stub, realizamos la llamada RPC
        // Como es un stream, iteramos hasta que no haya mas datos
        stub.saludoStream(peticion).forEachRemaining(respuesta -> {
            // Imprimimos la respuesta RPC
            System.out.println("Respuesta RPC: " + respuesta.getResultado());
        });
    }

    public static void saludarArchivoStream(ManagedChannel ch) {
        
        SaludoServiceGrpc.SaludoServiceBlockingStub stub = SaludoServiceGrpc.newBlockingStub(ch);

        SaludoRequest peticion = SaludoRequest.newBuilder().setNombre("archivote.csv").build();

        stub.saludoArchivoStream(peticion).forEachRemaining(respuesta -> {
            System.out.println(respuesta.getResultado());
        });

    }
}


