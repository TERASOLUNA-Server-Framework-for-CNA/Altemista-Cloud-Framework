
The Spring Web Flow example (see /WEB-INF/flows/example-flow.xml)
uses the views "webflow-example-step1" and "webflow-example-step2",
and expects them to be resolved the view resolver.

When the JavaServer Faces (JSF) feature is installed,
the default view resolver is replaced by the JavaServer Faces (JSF) view resolver,
so it is necessary to declare those views (i.e.: this .xhtml files).

If the Spring Web Flow feature is not installed, this files are not necessary.
