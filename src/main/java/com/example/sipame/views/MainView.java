package com.example.sipame.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout{
	
	public MainView() {
		Button btn = new Button("Clique me");
		add(btn);
	}

}
