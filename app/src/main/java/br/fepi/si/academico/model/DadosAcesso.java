package br.fepi.si.academico.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class DadosAcesso implements Serializable {
    private Map<String, String> cookies;
    private Map<String, String> data;

    public DadosAcesso() {
        this.cookies = new HashMap<>();
        this.data = new HashMap<>();
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
