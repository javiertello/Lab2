package servlets;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import todos.*;

@WebServlet(urlPatterns = { "/listtasks" })
public class ToDoGetServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

		
		
		
	}

	private String addToToDoList(String list, ToDo todo) {
		list +=   "<b>Task: </b>" + todo.getTask()
				+ "<br/><b>Context: </b>" + todo.getContext()
				+ "<br/><b>Project: </b>" +  todo.getProject()
				+ "<br/><b>Priority (1-10): </b>" + todo.getPriority() + "<br/><hr>";	
		return list;
	}

}
