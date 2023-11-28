package risks.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.lists.*;
import risks.utils.*;

@WebServlet(urlPatterns = {"/RiskFileServ","/RiskFile"})
public class RiskFileServ extends TopServlet{

    static String server_path="";
    static final long serialVersionUID = 24L;
    static Logger logger = LogManager.getLogger(RiskFileServ.class);
    int maxImageSize = 20000000, maxDocSize=20000000;
    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
    /**
     * Generates the main upload or view image form.
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req,res);
    }
    /**
     * @link #doGet
     * @see #doGet
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {
    
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	boolean success = true,
	    sizeLimitExceeded = false;
	String saveDirectory ="",file_path="";
	String newFile = "";
	String action="", date="", load_file="";
	String id="", risk_id = "", notes="", action2="";
	
	String message = "";
	int maxMemorySize = 20000000; // 30 MB , int of bytes
	int maxRequestSize = 20000000; // 30 MB
	String [] vals;
	User user = null;
	HttpSession session = null;
	long sizeInBytes = 0;
	// 
	// class to handle multipart request (for example text + image)
	// the image file or any upload file will be saved to the 
	// specified directory
	// 
	if(server_path.isEmpty() && context != null){
	    String str = context.getInitParameter("server_path");
	    if(str != null) server_path = str;
	}
	// we need this path for save purpose
	session = req.getSession(false);

	if(session != null){
	    user = (User)session.getAttribute("user");
	    if(user == null){
		String str = url+"Login";
		res.sendRedirect(str);
		return; 
	    }
	}
	else{
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return; 
	}
	// 
	// we have to make sure that this directory exits
	// if not we create it
	//
	File myDir = new File(server_path);
	if(!myDir.isDirectory()){
	    myDir.mkdirs();
	}
	// newFile = "spon"+month+day+seq; // no extension 
	// boolean isMultipart = ServletFileUpload.isMultipartContent(req);
	// System.err.println(" Multi "+isMultipart);
	//
	// Create a factory for disk-based file items
	DiskFileItemFactory factory = new DiskFileItemFactory();
	//
	// Set factory constraints
	factory.setSizeThreshold(maxMemorySize);
	//
	// if not set will use system temp directory
	// factory.setRepository(fileDirectory); 
	//
	// Create a new file upload handler
	ServletFileUpload upload = new ServletFileUpload(factory);
	// ServletFileUpload upload = new ServletFileUpload();
	//
	// Set overall request size constraint
	upload.setSizeMax(maxRequestSize);
	//
	String ext = "", old_name="";
	RiskFile riskFile = new RiskFile(debug);
	List<FileItem> items = null;
	String content_type = req.getContentType();
	try{
	    if(content_type != null && content_type.startsWith("multipart")){
		items = upload.parseRequest(req);
		Iterator<FileItem> iter = items.iterator();
		while (iter.hasNext()) {
		    FileItem item = iter.next();
		    if (item.isFormField()) {
			//
			// process form fields
			//
			name = item.getFieldName();
			value = item.getString();
			if (name.equals("id")){  
			    id = value;
			    riskFile.setId(value);
			}
			else if (name.equals("notes")) {
			    notes=value;
			    riskFile.setNotes(value);
			}
			else if (name.equals("risk_id")){ 
			    risk_id = value;
			    riskFile.setRisk_id(value);
			}
			else if (name.equals("load_file")) {
			    load_file =value.replace('+',' ');
			}
			else if(name.equals("action")){
			    action = value;
			}
		    }
		    else {
			// String mimType = Magic.getMagicMatch(item.get(), false).getMimeType();
			// System.err.println(" type "+mimType);
			//
			// process uploaded item/items
			//
			// String fieldName = item.getFieldName();
												
			String contentType = item.getContentType();
			// System.err.println(" context type "+contentType);
			if(Helper.mimeTypes.containsKey(contentType)){
			    ext = Helper.mimeTypes.get(contentType);
			}
			// System.err.println(" ext from type "+ext);
			sizeInBytes = item.getSize();
			String oldName = item.getName();
			String filename = "";
			// 
			logger.debug("file "+oldName);
			if (oldName != null && !oldName.equals("")) {
			    filename = FilenameUtils.getName(oldName);
			    old_name = filename;
			    if(ext.equals("")){
				ext = Helper.getFileExtensionFromName(filename);
				// System.err.println(" ext from name "+ext);
			    }
			    //
			    // create the file on the hard drive and save it
			    //
			    if(sizeInBytes > maxDocSize) 
				sizeLimitExceeded = true;
			    if(sizeLimitExceeded){
				message = " File Uploaded exceeds size limits "+
				    sizeInBytes;
				success = false;
			    }
			    else if(success){
				//
				// get a new name
				//
				riskFile.setOldName(old_name);
				riskFile.composeName(ext);
				newFile = riskFile.getName();
				if(!newFile.equals("")){
				    saveDirectory = riskFile.getFullPath(server_path, ext);
				    File file = new File(saveDirectory, newFile);
				    item.write(file);
				}
				else{
				    message = "Error: no file name assigned ";
				    success = false;
				}
			    }
			}
		    }
		}
	    }
	    else{
		Enumeration<String> values = req.getParameterNames();
		while (values.hasMoreElements()){
		    name = values.nextElement().trim();
		    vals = req.getParameterValues(name);
		    if(vals == null) continue;
		    value = vals[vals.length-1].trim();	
		    if (name.equals("id")){
			id = value;
			riskFile.setId(value);
		    }
		    else if(name.equals("risk_id")){
			risk_id = value;
			riskFile.setRisk_id(value);
		    }
		    else if(name.equals("notes")){
			riskFile.setNotes(value);
		    }
		    else if(name.equals("action")){
			action = value;
		    }	
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex);
	    success = false;
	    message += ex;
	}
	//
	if(action.equals("Save") && !sizeLimitExceeded){
	    date = Helper.getToday();
	    riskFile.setAddedBy(user);
	    String back = riskFile.doSave();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		id = riskFile.getId();
	    }
	}
	if(action.equals("Update")){
	    String back = riskFile.doUpdate();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	}
	else if(action.equals("Delete")){
	    String back = riskFile.doDelete();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		id="";
	    }
	}	
	else if(action.equals("download")){
	    String back = riskFile.doSelect();
	    String filename = riskFile.getName();
	    String filePath = riskFile.getPath(server_path);
	    filePath += filename;
	    doDownload(req, res, filePath, riskFile);
	    return;
	}
	else if(action.equals("") && !id.equals("")){
	    String back = riskFile.doSelect();
	    risk_id = riskFile.getRisk_id();
	}
	RelatedUtil related = null;
	List<RiskFile> files = null;
	if(!risk_id.equals("")){
	    related = new RelatedUtil(debug, risk_id, url);
	    RiskFileList rfl = new RiskFileList(debug, risk_id);
	    String back = rfl.find();
	    if(!back.equals("")){
		message += back;
	    }
	    else{
		List<RiskFile> ones = rfl.getFiles();
		if(ones != null && ones.size() > 0){
		    files = ones;
		}
	    }
	}
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	out.println("<h3 class=\"titleBar\">File Upload "+id+"</h3>");
	out.println("<div id=\"mainContent\">");
	out.println("<script language=javascript>");
	out.println("  function validateForm(){		         ");
       	out.println("     return true;				         ");
	out.println("	}	         			             ");
	out.println(" </script>		                         ");
	if(success){
	    if(!message.equals(""))
		out.println("<h3>"+message+"</h3>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h3><font color=\"red\">"+message+"</font></h3>");
	}
	out.println("<form name=\"myForm\" method=\"post\" "+
		    "ENCTYPE=\"multipart/form-data\" >");
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	}
	if(!risk_id.equals("")){
	    out.println("<input type=\"hidden\" name=\"risk_id\" value=\""+risk_id+"\" />");
	}	
	//
	out.println("<fieldset><legend>Upload Files</legend>");
	out.println("<table border=\"1\" width=\"75%\">");
	out.println("<tr><td>");
	//
	out.println("<table width=\"100%\">");

	if(related != null && related.foundType()){
	    out.println("<tr><td><label>Related "+related.getType()+" :</label>"+related.getLink()+"</td></tr>");						
	}
	else{
	    out.println("<tr><td><label>Related Risk ID: </label>");						
	    out.println(risk_id+"</td></tr>");
	}
	if(id.equals("")){
	    out.println("<tr><td>"+
			"To upload a new document "+
			"<ul>"+
			"<li>Download it to your computer </li>"+
			"<li>Click on the Browse button to locate this file"+
			" on your computer.</li> "+
			"<li> Click on Save.</li>"+
			"<li> A new link to the uploaded "+
			" file will be shown below.</li>"+
			"<li> Supported documents are images, MS Documents, PDF;s, web pages, spread sheets, etc </li>"+
			"</ul>");
	    out.println("</td></tr>");
	    out.println("<tr><td><label>File </label> "); 
	    out.println("<input type=\"file\" name=\"load_file\" "+
			" size=\"30\"></td></tr>");
	    out.println("<tr><td class=\"left\"><label>Notes </label></td></tr>");
	    out.println("<tr><td class=\"left\">"); 
	    out.println("<textarea name=\"notes\" cols=\"70\" rows=\"5\" wrap=\"wrap\">");
	    out.println("</textarea></td></tr>");
	    out.println("</table></td></tr>");												
	    out.println("<tr><td align=\"right\">  "+
			"<input type=\"submit\" name=\"action\" "+
			"value=\"Save\">"+
			"</td></tr>");

	}
	else{
	    out.println("<tr><td><label>Date: </label>"+riskFile.getDate()+"</td></tr>");
	    out.println("<tr><td><label>Added By: </label>"+riskFile.getAddedBy()+"</td></tr>");						
	    out.println("<tr><td><label>File: </label> <a href=\""+url+"RiskFileServ?id="+id+"&action=download\"> "+riskFile.getOldName()+"</a> </td></tr>");
	    if(riskFile.hasNotes()){
		out.println("<tr><td><label>Notes: </label>"+riskFile.getNotes()+"</td></tr>");
	    }
	    out.println("<tr>");
	    out.println("<td align=\"right\">  "+
			"<input type=\"submit\" name=\"action\" "+
			"onclick=\"validateDelete();\" "+
			"value=\"Delete\">"+
			"</td>");
	    out.println("</tr>");				
	}

	out.println("</table></td></tr>");
	out.println("</table>");				
	out.println("</fieldset>");
	out.println("</form>");
	if(files != null && files.size() > 0){
	    Helper.printFiles(out, url, files);
	}
	//
	// send what we have so far
	//
	out.print("</body></html>");
	out.close();

    }

    void doDownload(HttpServletRequest request,
		    HttpServletResponse response,
		    String inFile,
		    RiskFile riskFile){
		
	BufferedInputStream input = null;
	BufferedOutputStream output = null;
	try{
	    //
	    // Decode the file name (might contain spaces and so on) and prepare file object.
	    // File file = new File(filePath, URLDecoder.decode(inspFile, "UTF-8"));
	    File file = new File(inFile);
	    //
	    // Check if file actually exists in filesystem.
	    //
	    if (!file.exists()) {
		// Do your thing if the file appears to be non-existing.
		// Throw an exception, or send 404, or show default/warning page, or just ignore it.
		response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
		return;
	    }
	    //
	    // Get content type by filename.
	    String contentType = context.getMimeType(file.getName());
	    //
	    // To add new content types, add new mime-mapping entry in web.xml.
	    if (contentType == null) {
		contentType = "application/octet-stream";
	    }
	    //			
	    // Init servlet response.
	    response.reset();
	    response.setBufferSize(DEFAULT_BUFFER_SIZE);
	    response.setContentType(contentType);
	    response.setHeader("Content-Length", String.valueOf(file.length()));
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + (riskFile.getOldName().equals("")?riskFile.getName():riskFile.getOldName()) + "\"");
	    //
	    // Prepare streams.
	    //
            // Open streams.
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
            // Write file contents to response.
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
	}
	catch(Exception ex){
	    logger.error(ex);
        } finally {
	    close(output);
            close(input);
        }
    }	
    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }
}






















































