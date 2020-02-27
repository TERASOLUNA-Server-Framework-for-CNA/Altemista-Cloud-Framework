
Any static resource at "/META-INF/resources" in the classpath
will be served by Spring's DispatchServlet at the "/*" mapping.

Do not put any JSP here; they would be served as a static resource (i.e.: the page will not be rendered).
