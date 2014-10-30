package toDo_REST;

import java.io.FileWriter;
import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;

import toDo_REST.ToDoList;
import com.google.gson.Gson;

@Path("/todos")
public class ToDoWebService {
	
	public final static String DEFAULT_FILE_NAME = "todo_list.json";
	
	/**
	 * The (shared) ToDo list object. 
	 */
	@Inject
	ToDoList todoList;
	
	/**
	 * A GET /todos request should return the  ToDo list in JSON.
	 * @return a JSON representation of the ToDo list.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ToDoList getToDoList() {
		return todoList;
	}
	
	/**
	 * A POST /todos request should add a new entry to the ToDo list.
	 * @param info the URI information of the request
	 * @param person the posted entity
	 * @return a JSON representation of the new entry that should be available at /todos/todo/{id}.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addToDo(@Context UriInfo info, ToDo todo) {
		todoList.getList().add(todo);
		todo.setId(todoList.nextId());
		todo.setHref(info.getAbsolutePathBuilder().path("todo/{id}").build(todo.getId()));
		saveList();
		return Response.created(todo.getHref()).entity(todo).build();
	}

	/**
	 * A GET /todos/todo/{id} request should return a entry from the ToDo list
	 * @param id the unique identifier of a ToDo task
	 * @return a JSON representation of the new entry or 404
	 */
	@GET
	@Path("/todo/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getToDo(@PathParam("id") int id) {
		for (ToDo t : todoList.getList()) {
			if (t.getId() == id) {
				return Response.ok(t).build();
			}
		}
		return Response.status(Status.NOT_FOUND).build();
	}

	/**
	 * A PUT /todos/todo/{id} should update a entry if exists
	 * @param info the URI information of the request
	 * @param ToDo the posted entity
	 * @param id the unique identifier of a ToDo task
	 * @return a JSON representation of the new updated entry or 400 if the id is not a key
	 */
	@PUT
	@Path("/todo/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateToDo(@Context UriInfo info,
			@PathParam("id") int id, ToDo todo) {
		for (int i = 0; i < todoList.getList().size(); i++) {
			if (todoList.getList().get(i).getId() == id) {
				todo.setId(id);
				todo.setHref(info.getAbsolutePath());
				todoList.getList().set(i, todo);
				saveList();
				return Response.ok(todo).build();
			}
		}
		return Response.status(Status.BAD_REQUEST).build();
	}

	/**
	 * A DELETE /todos/todo/{id} should delete a entry if exists
	 * @param id the unique identifier of a ToDo task
	 * @return 204 if the request is successful, 404 if the id is not a key
	 */
	@DELETE
	@Path("/todo/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateToDo(@PathParam("id") int id) {
		for (int i = 0; i < todoList.getList().size(); i++) {
			if (todoList.getList().get(i).getId() == id) {
				todoList.getList().remove(i);
				saveList();
				return Response.noContent().build();
			}
		}
		return Response.status(Status.NOT_FOUND).build();
	}
	
	public void saveList() {
		// Write the new ToDo list back to disk.
		Gson gson = new Gson();
		String filename = DEFAULT_FILE_NAME;
		FileWriter output;
		try {
			output = new FileWriter(filename);
			output.write(gson.toJson(todoList));
			output.close();
		} catch (IOException e) {
			System.out.println("Error al escribir en el fichero JSON");
		}
	}
}
