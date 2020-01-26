import org.junit.jupiter.api.Test;

public class InjectCodeTest {

	@Test
	public void testTwoMethods() {
		String code = "public void name1() { return a; } public void name2() { str = \"}\";block; } public void name3(int a, int b) { return a + b; }   public void name4(int a, int x) {return a;} ";
		System.out.println(code);
		InjectCode injectCode = new InjectCode();
		String result = injectCode.injectCodeMethods(code.toCharArray(), "log", "feature");
		System.out.println(result);
	}
	
	//@Test
	public void testOndeMethod() {
		InjectCode injectCode = new InjectCode();
		String methodFile = "public void name(@(\"sdsd\")String a, String b) { return a; return b; \\* sadads *\\ block; }";
		String expected = "public void name(@(\"sdsd\")String a, String b) { sizeOf(a,b);  block; }";
		String result = injectCode.injectCodeMethods(methodFile.toCharArray(), "log", "feature");
		System.out.println(result);
		//assertEquals(expected, result);
	}
	
	/**
	@Test
	public void testTwoMethods() {
		InjectCode injectCode = new InjectCode();
		String methodFile = "public void name(@(\"sdsd\")String a, String b) { block; } \n public void name(    int x,     String y, int w       ) thrwos Exception {  block; }";
		String expected =   "public void name(@(\"sdsd\")String a, String b) { sizeOf(a,b);  block; } \n public void name(    int x,     String y, int w       ) thrwos Exception { sizeOf(x,y,w);   block; }\n";
		String result = injectCode.injectCode(methodFile.toCharArray());
		System.out.println(result);
		System.out.println(expected);
		assertEquals(expected, result);
	}

	
	//@Test
	public void callMethod() {
		InjectCode injectCode = new InjectCode();
		String methodFile = "public void name(@(\"sdsd\")String a, String b) { some(a); }";
		String expected = "public void name(@(\"sdsd\")String a, String b) { sizeOf(a,b);  some(a); }";
		String result = injectCode.injectCodeMethods(methodFile.toCharArray());
		assertEquals(expected, result);
	}
	
	*/
	
	//@Test
	public void test() {
		String str = "package ibase.rest.api.algorithm.v1;\n" + 
				"\n" + 
				"import ibase.rest.model.algorithm.v1.*;\n" + 
				"import ibase.rest.api.algorithm.v1.AlgorithmsApiService;\n" + 
				"import ibase.rest.api.algorithm.v1.factories.AlgorithmsApiServiceFactory;\n" + 
				"\n" + 
				"import io.swagger.annotations.ApiParam;\n" + 
				"import io.swagger.jaxrs.*;\n" + 
				"\n" + 
				"import ibase.rest.model.algorithm.v1.Algorithm;\n" + 
				"import ibase.rest.model.algorithm.v1.AlgorithmVersion;\n" + 
				"import ibase.rest.model.algorithm.v1.AlgorithmConfiguration;\n" + 
				"import ibase.rest.model.algorithm.v1.AlgorithmDocumentation;\n" + 
				"import ibase.rest.model.algorithm.v1.Error;\n" + 
				"\n" + 
				"import java.util.List;\n" + 
				"import ibase.rest.api.algorithm.v1.NotFoundException;\n" + 
				"\n" + 
				"import java.io.InputStream;\n" + 
				"\n" + 
				"import org.glassfish.jersey.media.multipart.FormDataContentDisposition;\n" + 
				"import org.glassfish.jersey.media.multipart.FormDataParam;\n" + 
				"\n" + 
				"import javax.ws.rs.core.Context;\n" + 
				"import javax.ws.rs.core.Response;\n" + 
				"import javax.ws.rs.core.SecurityContext;\n" + 
				"import javax.ws.rs.*;\n" + 
				"\n" + 
				"@Path(\"/algorithms\")\n" + 
				"\n" + 
				"@Produces({ \"application/json\" })\n" + 
				"@io.swagger.annotations.Api(description = \"the algorithms API\")\n" + 
				"@javax.annotation.Generated(value = \"class io.swagger.codegen.languages.JavaJerseyServerCodegen\", date = \"2019-12-06T18:10:43.665-02:00\")\n" + 
				"public class AlgorithmsApi  {\n" + 
				"   private final AlgorithmsApiService delegate = AlgorithmsApiServiceFactory.getAlgorithmsApi();\n" + 
				"\n" + 
				"    @GET\n" + 
				"    @Path(\"/{algorithmId}\")\n" + 
				"    \n" + 
				"    @Produces({ \"application/json\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Find an algorithm by its ID\", notes = \"This endpoint returns an algorithm by its ID. The ID is encoded in Base64.\", response = Algorithm.class, tags={ \"Algorithms\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"Successful operation\", response = Algorithm.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 404, message = \"Algorithm not found.\", response = Algorithm.class) })\n" + 
				"    public Response algorithmsAlgorithmIdGet(@ApiParam(value = \"The ID of an algorithm.\",required=true) @PathParam(\"algorithmId\") String algorithmId\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\") @QueryParam(\"locale\") String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.algorithmsAlgorithmIdGet(algorithmId,locale,securityContext);\n" + 
				"    }\n" + 
				"    @GET\n" + 
				"    @Path(\"/{algorithmId}/versions\")\n" + 
				"    \n" + 
				"    @Produces({ \"application/json\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"List the versions of an algorithm.\", notes = \"This endpoint returns the list of the versions available for a specific algorithm.\", response = AlgorithmVersion.class, responseContainer = \"List\", tags={ \"Algorithms\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"Successful operation.\", response = AlgorithmVersion.class, responseContainer = \"List\"),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 403, message = \"if the user is not allowed to access this algorithm\", response = AlgorithmVersion.class, responseContainer = \"List\") })\n" + 
				"    public Response algorithmsAlgorithmIdVersionsGet(@ApiParam(value = \"The ID of an algorithm.\",required=true) @PathParam(\"algorithmId\") String algorithmId\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\") @QueryParam(\"locale\") String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.algorithmsAlgorithmIdVersionsGet(algorithmId,locale,securityContext);\n" + 
				"    }\n" + 
				"    @GET\n" + 
				"    @Path(\"/{algorithmId}/versions/{versionId}/configuration\")\n" + 
				"    \n" + 
				"    @Produces({ \"application/json\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Get the information about the parameter configuration of an algorithm version.\", notes = \"This endpoint returns the detail information about an algorithm version parameter configuration. The parameter configuration is the set of groups with input and output parameters required for the algorithm execution.\", response = AlgorithmConfiguration.class, tags={ \"Algorithms\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"Successful operation.\", response = AlgorithmConfiguration.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 400, message = \"If the version identification provided in the versionId parameter does not exist or if the algorithm identification provided in algorithmId does not exist.\", response = AlgorithmConfiguration.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 403, message = \"If the authenticated user is not authorized to do this operation\", response = AlgorithmConfiguration.class) })\n" + 
				"    public Response algorithmsAlgorithmIdVersionsVersionIdConfigurationGet(@ApiParam(value = \"The ID of an algorithm.\",required=true) @PathParam(\"algorithmId\") String algorithmId\n" + 
				",@ApiParam(value = \"The ID of an algorithm version.\",required=true) @PathParam(\"versionId\") String versionId\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\") @QueryParam(\"locale\") String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.algorithmsAlgorithmIdVersionsVersionIdConfigurationGet(algorithmId,versionId,locale,securityContext);\n" + 
				"    }\n" + 
				"    @GET\n" + 
				"    @Path(\"/{algorithmId}/versions/{versionId}/documentation/download/\")\n" + 
				"    \n" + 
				"    @Produces({ \"application/json\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Download a algorithm version documentation file\", notes = \"This endpoint returns the documentation file of a algorithm version.\", response = void.class, tags={ \"Algorithms\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"The specified algorithms's binary\", response = void.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 400, message = \"invalid version format\", response = void.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 404, message = \"if not found the specified combination of algorithmId, versionId and platformId\", response = void.class) })\n" + 
				"    public Response algorithmsAlgorithmIdVersionsVersionIdDocumentationDownloadGet(@ApiParam(value = \"The ID of an algorithm.\",required=true) @PathParam(\"algorithmId\") String algorithmId\n" + 
				",@ApiParam(value = \"The ID of an algorithm version.\",required=true) @PathParam(\"versionId\") String versionId\n" + 
				",@ApiParam(value = \"The name of the file to be downloaded from the algorithm version documentation.\",required=true) @QueryParam(\"fileName\") String fileName\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\") @QueryParam(\"locale\") String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.algorithmsAlgorithmIdVersionsVersionIdDocumentationDownloadGet(algorithmId,versionId,fileName,locale,securityContext);\n" + 
				"    }\n" + 
				"    @GET\n" + 
				"    @Path(\"/{algorithmId}/versions/{versionId}\")\n" + 
				"    \n" + 
				"    @Produces({ \"application/json\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Get the information about a member of a project team.\", notes = \"This endpoint returns the detail information about an algorithm version.\", response = AlgorithmVersion.class, tags={ \"Algorithms\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"Successful operation.\", response = AlgorithmVersion.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 400, message = \"If the version identification provided in the versionId parameter does not exist or if the algorithm identification provided in algorithmId does not exist.\", response = AlgorithmVersion.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 403, message = \"If the authenticated user is not authorized to do this operation\", response = AlgorithmVersion.class) })\n" + 
				"    public Response algorithmsAlgorithmIdVersionsVersionIdGet(@ApiParam(value = \"The ID of an algorithm.\",required=true) @PathParam(\"algorithmId\") String algorithmId\n" + 
				",@ApiParam(value = \"The ID of an algorithm version.\",required=true) @PathParam(\"versionId\") String versionId\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\") @QueryParam(\"locale\") String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.algorithmsAlgorithmIdVersionsVersionIdGet(algorithmId,versionId,locale,securityContext);\n" + 
				"    }\n" + 
				"    @GET\n" + 
				"    @Path(\"/{algorithmId}/versions/{versionId}/platform/{platformId}/download\")\n" + 
				"    \n" + 
				"    @Produces({ \"application/json\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Download a algorithms's binary\", notes = \"\", response = void.class, tags={ \"Algorithms\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"The specified algorithms's binary\", response = void.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 400, message = \"invalid version format\", response = void.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 404, message = \"if not found the specified combination of algorithmId, versionId and platformId\", response = void.class) })\n" + 
				"    public Response algorithmsAlgorithmIdVersionsVersionIdPlatformPlatformIdDownloadGet(@ApiParam(value = \"The ID of an algorithm.\",required=true) @PathParam(\"algorithmId\") String algorithmId\n" + 
				",@ApiParam(value = \"The ID of an algorithm version.\",required=true) @PathParam(\"versionId\") String versionId\n" + 
				",@ApiParam(value = \"The platform of algorithm.\",required=true) @PathParam(\"platformId\") String platformId\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\") @QueryParam(\"locale\") String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.algorithmsAlgorithmIdVersionsVersionIdPlatformPlatformIdDownloadGet(algorithmId,versionId,platformId,locale,securityContext);\n" + 
				"    }\n" + 
				"    @GET\n" + 
				"    @Path(\"/documentation/\")\n" + 
				"    \n" + 
				"    @Produces({ \"application/json\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Download all algorithm documentation files\", notes = \"This endpoint returns the documentation of all algorithms as a json file.\", response = AlgorithmDocumentation.class, responseContainer = \"List\", tags={ \"Algorithms\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"The documentation of all algorithms\", response = AlgorithmDocumentation.class, responseContainer = \"List\"),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 400, message = \"invalid version format\", response = AlgorithmDocumentation.class, responseContainer = \"List\"),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 404, message = \"if not found the specified combination of algorithmId, versionId and platformId\", response = AlgorithmDocumentation.class, responseContainer = \"List\") })\n" + 
				"    public Response algorithmsDocumentationGet(@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\") @QueryParam(\"locale\") String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.algorithmsDocumentationGet(locale,securityContext);\n" + 
				"    }\n" + 
				"    @GET\n" + 
				"    \n" + 
				"    \n" + 
				"    @Produces({ \"application/json\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"List the available algorithms.\", notes = \"This endpoint returns information about the algorithms available for submission on execution environments. The response includes the algoritm name and other details about each algoritm. \", response = Algorithm.class, responseContainer = \"List\", tags={ \"Algorithms\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"An array of algorithms\", response = Algorithm.class, responseContainer = \"List\"),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"Unexpected error\", response = Algorithm.class, responseContainer = \"List\") })\n" + 
				"    public Response algorithmsGet(@ApiParam(value = \"Filter the algorithms by the begining of the name provided. Only the algorithms which name begins with the filter name will be returned. This filter is not case sensitive. If this filter is not provided, the response list all the algorithms available.\") @QueryParam(\"name\") String name\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\") @QueryParam(\"locale\") String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.algorithmsGet(name,locale,securityContext);\n" + 
				"    }\n" + 
				"    @GET\n" + 
				"    @Path(\"/public/{algorithmId}/documentation/download/\")\n" + 
				"    \n" + 
				"    @Produces({ \"application/json\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Download a public algorithm version documentation file. The documentation is related to the last version of the algorithm.\", notes = \"This endpoint returns the documentation file of the last version of an algorithm, without authentication\", response = void.class, tags={ \"Algorithms\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"The specified public algorithms's documentation file\", response = void.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 404, message = \"if the algorithm was not found\", response = void.class) })\n" + 
				"    public Response algorithmsPublicAlgorithmIdDocumentationDownloadGet(@ApiParam(value = \"The ID of an algorithm.\",required=true) @PathParam(\"algorithmId\") String algorithmId\n" + 
				",@ApiParam(value = \"The name of the file to be downloaded from the public algorithm version documentation.\",required=true) @QueryParam(\"fileName\") String fileName\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\") @QueryParam(\"locale\") String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.algorithmsPublicAlgorithmIdDocumentationDownloadGet(algorithmId,fileName,locale,securityContext);\n" + 
				"    }\n" + 
				"    @GET\n" + 
				"    @Path(\"/public/documentation/\")\n" + 
				"    \n" + 
				"    @Produces({ \"application/json\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Download all public algorithm documentation files\", notes = \"This endpoint returns the documentation of all public algorithms as a json file.\", response = AlgorithmDocumentation.class, responseContainer = \"List\", tags={ \"Algorithms\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"The documentation of all public algorithms\", response = AlgorithmDocumentation.class, responseContainer = \"List\") })\n" + 
				"    public Response algorithmsPublicDocumentationGet(@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\") @QueryParam(\"locale\") String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.algorithmsPublicDocumentationGet(locale,securityContext);\n" + 
				"    }\n" + 
				"    @GET\n" + 
				"    @Path(\"/tools\")\n" + 
				"    \n" + 
				"    @Produces({ \"application/json\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Download a algorithms's tools\", notes = \"\", response = void.class, tags={ \"Algorithms\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"The specified tools's contents\", response = void.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 400, message = \"if was invalid path\", response = void.class) })\n" + 
				"    public Response algorithmsToolsGet(@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\") @QueryParam(\"locale\") String locale\n" + 
				",@ApiParam(value = \"relative path to download de tools.\") @QueryParam(\"path\") String path\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.algorithmsToolsGet(locale,path,securityContext);\n" + 
				"    }\n" + 
				"}";
				InjectCode injectCode = new InjectCode();
				String result = injectCode.injectCodeMethods( str.toCharArray(), "log", "feature");
				System.out.println(result);
	}
}
