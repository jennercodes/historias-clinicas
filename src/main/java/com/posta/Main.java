package com.posta;

import com.posta.ui.Contexto;
import com.posta.ui.LoginFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Contexto contexto = new Contexto();
        new LoginFrame(contexto).setVisible(true);
    }
}
