package br.fepi.si.carona.controller;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Conexao {

    public static String postDados(String urlUsuario, String parametrosUsuario){
        URL url;
        HttpURLConnection connection = null;

        try {
            url = new URL(urlUsuario);//pega a url passada pelo usuario

            connection = (HttpURLConnection) url.openConnection();//abre a conexão

            //PROPRIEDADES DA COMUNICAÇÃO
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//tipo da comunicação(utilizado: Formulário)

            connection.setRequestProperty("Content-Lenght", "" + Integer.toString(parametrosUsuario.getBytes().length));//tamanho da informação

            connection.setRequestProperty("Content-Language", "pt-BR");//idioma

            connection.setDefaultUseCaches(false);//não armazena em cache
            connection.setDoInput(true);//habilita entrada de dados
            connection.setDoOutput(true);//habilita saida de dados


            //FAZ A SOLICITAÇÃO ATRAVÉS DOS PARÂMETROS DO USUÁRIO
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            outputStreamWriter.write(parametrosUsuario);
            outputStreamWriter.flush();

            Log.e("URL", connection.getResponseMessage());

            //OBTÉM DADOS DA SOLICITAÇÃO
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));//armazena os dados
            String linha;//pega cada informação no while
            StringBuffer resposta = new StringBuffer();//junta todas as informações

            while((linha = bufferedReader.readLine()) != null) {
                resposta.append(linha);
                resposta.append("\n");
            }

            bufferedReader.close();

            return resposta.toString();

        }catch (Exception erro){
            return null;

        }finally {
            if(connection != null) {
                connection.disconnect();
            }
        }

    }


}
