package br.com.suportecpl.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/imagensTools/*")
public class ImagesViewServlet extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try{
		    String filename = request.getPathInfo().substring(1);
		    File file = new File("c:\\Users\\" + System.getProperty("user.name") + "\\tools\\tool-img", filename);
		    response.setHeader("Content-Type", getServletContext().getMimeType(filename));
		    response.setHeader("Content-Length", String.valueOf(file.length()));
		    response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
		    Files.copy(file.toPath(), response.getOutputStream());
		}catch (Exception e) {
			// TODO: handle exception
		}

	}
}
