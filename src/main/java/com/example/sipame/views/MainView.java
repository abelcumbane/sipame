package com.example.sipame.views;

import java.util.stream.Stream;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayout.Section;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;

@Route("")
@CssImport("./styles/styles.css")
public class MainView extends AppLayout {

	private final HorizontalLayout topHeader = new HorizontalLayout();
    private final HorizontalLayout dynamicHeader = new HorizontalLayout();
    private Button gestaoBtn;
    private Button projectoBtn;
    private Button selectedButton;
    
    public MainView() {
        setPrimarySection(Section.DRAWER);
        createHeader();
        createDrawer();
    }
    
    private void createHeader() {
        topHeader.setWidthFull();
        topHeader.setPadding(true);
        topHeader.getStyle().set("background-color", "#86bdf7");
        
        Span userLabel = new Span("");
        Button logout = new Button("SIPAME = SIstema de PAgamento de MEnsalidades", e-> Notification.show("Ainda nao implementado"));
        logout.getStyle().set("background-color", "#4295ed").set("color", "#ffffff");
       
        
        topHeader.add(new HorizontalLayout(userLabel,logout));
        topHeader.setJustifyContentMode(JustifyContentMode.BETWEEN);
    	
        //addToNavbar(topHeader);

        dynamicHeader.setSpacing(true);
        dynamicHeader.setPadding(true);
        dynamicHeader.setWidthFull();
        dynamicHeader.getStyle().set("background-color", "#ffffff"); 

        // Envolva os dois em um layout vertical
        VerticalLayout headerContainer = new VerticalLayout(topHeader, dynamicHeader);
        headerContainer.setPadding(false);
        headerContainer.setSpacing(false);
        headerContainer.setWidthFull();

        addToNavbar(headerContainer);
    }

    
    private void createDrawer() {
        VerticalLayout menu = new VerticalLayout();
        menu.setPadding(true);
        //menu.getStyle().set("background-color", "#86bdf7");


        Image logo = new Image("images/in-Bytes.png", "Logo");
        logo.setWidth("100px");
        menu.add(logo);

        gestaoBtn = new Button("Gestão", e -> {
            setSelectedButton(gestaoBtn);
            showGestaoLinks();
        });
        projectoBtn = new Button("Projecto", e -> {
            setSelectedButton(projectoBtn);
            showProjectoLinks();
        });

        menu.add(gestaoBtn, projectoBtn);
        addToDrawer(menu);
        
        // Estilo padrão dos botões
        Stream.of(gestaoBtn, projectoBtn).forEach(btn -> {
            btn.getStyle()
               .set("color", "white")
               .set("background-color", "#c9c3d9")
               .set("border", "none");
               //.set("width", "100%");
               //.set("text-align", "left");
        });
        
        
        getElement().executeJs("""
                const drawer = this.shadowRoot.querySelector('[part="drawer"]');
                if (drawer) {
                    drawer.style.backgroundColor = '#f0f4f7';
                }
            """);
    }

    private void setSelectedButton(Button selected) {
        gestaoBtn.removeClassName("selected-button");
        projectoBtn.removeClassName("selected-button");
        selected.addClassName("selected-button");
        
        if (selectedButton != null) {
            selectedButton.getStyle()
                .set("background-color", "transparent")
                .set("font-weight", "normal");
        
        }
        
        selectedButton = selected;
        selectedButton.getStyle()
            .set("background-color", "#1a73e8")
            .set("font-weight", "bold");
    }

    private void showGestaoLinks() {
        dynamicHeader.removeAll();
        
     // Título em barra
        Span titulo = new Span("Painel de Gestão de:");
        titulo.getStyle()
              .set("color", "white")
              .set("font-weight", "bold")
              .set("font-size", "16px");

        Div tituloBar = new Div(titulo);
        tituloBar.setWidthFull();
        tituloBar.getStyle()
                 .set("background-color", "#4295ed")
                 .set("padding", "10px")
                 .set("border-radius", "6px")
                 .set("box-shadow", "0px 2px 4px rgba(0, 0, 0, 0.1)")
                 .set("margin-bottom", "5px");
        
        
        
        HorizontalLayout links = new HorizontalLayout(
            createDynamicLink("Alunos", AlunoView.class),
            createDynamicLink("Mensalidades", MensalidadeView.class),
            createDynamicLink("Pagamentos", PagamentoView.class)
        );
        links.setSpacing(true);

        VerticalLayout headerContent = new VerticalLayout(tituloBar, links);
        headerContent.setSpacing(false);
        headerContent.setPadding(false);

        dynamicHeader.add(headerContent); 
    }

    private void showProjectoLinks() {
        dynamicHeader.removeAll();
        dynamicHeader.add(
            createDynamicLink("Projectos", ProjectosView.class),
            createDynamicLink("Projectos Activos", ProjectosActivosView.class)
        );
    }

    private RouterLink createDynamicLink(String text, Class<? extends com.vaadin.flow.component.Component> navigationTarget) {
        RouterLink link = new RouterLink(text, navigationTarget);
        String currentRoute = UI.getCurrent().getInternals().getActiveViewLocation().getPath();
        String targetRoute = RouteConfiguration.forApplicationScope().getUrl(navigationTarget);

        if (currentRoute.equals(targetRoute)) {
            link.addClassName("active-link");
        }

        link.getElement().getStyle()
        .set("background-color", "#86bdf7")
        .set("color", "white")
        .set("padding", "8px 16px")
        .set("border-radius", "5px")
        .set("text-decoration", "none")
        .set("display", "inline-block")
        .set("margin-right", "8px");
        
        if (currentRoute.equals(targetRoute)) {
            // Estilo de link ativo
            link.getElement().getStyle()
                .set("background-color", "#1a73e8")
                .set("color", "white")
                .set("font-weight", "bold");
        } else {
            // Estilo normal
            link.getElement().getStyle()
                .set("background-color", "#86bdf7")
                .set("color", "white");
        }
        return link;
    }
}

