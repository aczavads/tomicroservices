import java.io.File;

import org.junit.jupiter.api.Test;

public class InjectCodeTest {

	//@Test
	public void testMethods() {
		String code = "package csbase.rest.adapter.job.v1.util;\n" + 
				"\n" + 
				"import java.io.ByteArrayOutputStream;\n" + 
				"import java.io.IOException;\n" + 
				"import java.nio.ByteBuffer;\n" + 
				"import java.util.ArrayList;\n" + 
				"import java.util.List;\n" + 
				"import java.util.stream.Collectors;\n" + 
				"\n" + 
				"import br.pucrio.tecgraf.soma.job.Algorithm;\n" + 
				"import br.pucrio.tecgraf.soma.job.AlgorithmParameter;\n" + 
				"import br.pucrio.tecgraf.soma.job.ExitStatus;\n" + 
				"import br.pucrio.tecgraf.soma.job.Flow;\n" + 
				"\n" + 
				"import csbase.logic.CommandFinalizationInfo;\n" + 
				"import csbase.logic.CommandFinalizationType;\n" + 
				"import csbase.logic.CommandInfo;\n" + 
				"import csbase.logic.ExtendedCommandFinalizationInfo;\n" + 
				"import csbase.logic.algorithms.AlgorithmConfigurator;\n" + 
				"import csbase.logic.algorithms.flows.configurator.FlowAlgorithmConfigurator;\n" + 
				"import csbase.logic.algorithms.flows.configurator.Node;\n" + 
				"import csbase.logic.algorithms.parameters.SimpleAlgorithmConfigurator;\n" + 
				"import csbase.logic.algorithms.parameters.SimpleParameter;\n" + 
				"import csbase.logic.algorithms.serializer.FlowAlgorithmConfigurationSerializer;\n" + 
				"import csbase.logic.algorithms.serializer.exception.AlgorithmConfigurationSerializerException;\n" + 
				"import csbase.server.plugin.service.commandpersistenceservice.ICommandInfo;\n" + 
				"\n" + 
				"/**\n" + 
				" * Classe para converter os dados do comando CSBase para o tipo definido no\n" + 
				" * schema.\n" + 
				" */\n" + 
				"public class CSBaseCommandUtil {\n" + 
				"\n" + 
				"	/**\n" + 
				"	 * Converte um parâmetro do CSBase para o tipo definido no schema\n" + 
				"	 *\n" + 
				"	 * @param p\n" + 
				"	 *            o parâmetro vendo do CSBase\n" + 
				"	 * @return o parâmetro convertido para o schema\n" + 
				"	 */\n" + 
				"	public static AlgorithmParameter convertFromCSBaseParameter(SimpleParameter<?> p) {\n" + 
				"		AlgorithmParameter param = new AlgorithmParameter();\n" + 
				"		param.setParameterId(p.getName());\n" + 
				"		param.setLabel(p.getLabel());\n" + 
				"		param.setType(p.getType());\n" + 
				"		param.setValue(p.getValueAsText());\n" + 
				"		return param;\n" + 
				"	}\n" + 
				"\n" + 
				"	/**\n" + 
				"	 * Cria os dados de fluxo a partir do configurador do fluxo.\n" + 
				"	 * \n" + 
				"	 * @param flowConf\n" + 
				"	 *            configurador do fluxo\n" + 
				"	 * @return os dados do fluxo para a mensagem.\n" + 
				"	 */\n" + 
				"	public static Flow createFlowFromConfig(FlowAlgorithmConfigurator flowConf) {\n" + 
				"		Flow flow = new Flow();\n" + 
				"		// TODO: Pegar os dados de fluxo instalado do serviço de algoritmos\n" + 
				"		if (flowConf.getAlgorithmId() != null) {\n" + 
				"			flow.setFlowId(flowConf.getAlgorithmId());\n" + 
				"		} else {\n" + 
				"			flow.setFlowId(\"\");\n" + 
				"		}\n" + 
				"\n" + 
				"		if (flowConf.getAlgorithmVersion() != null) {\n" + 
				"			flow.setFlowVersion(flowConf.getAlgorithmVersion().toString());\n" + 
				"		} else {\n" + 
				"			flow.setFlowVersion(\"\");\n" + 
				"		}\n" + 
				"\n" + 
				"		if (flowConf.getAlgorithmName() != null) {\n" + 
				"			flow.setFlowName(flowConf.getAlgorithmName());\n" + 
				"		} else {\n" + 
				"			flow.setFlowName(\"\");\n" + 
				"		}\n" + 
				"\n" + 
				"		List<Algorithm> algorithms = new ArrayList<>();\n" + 
				"		for (Node node : flowConf.getNodes()) {\n" + 
				"			algorithms.add(createAlgorithmFromConfig(node.getConfigurator()));\n" + 
				"		}\n" + 
				"		flow.setAlgorithms(algorithms);\n" + 
				"\n" + 
				"		FlowAlgorithmConfigurationSerializer serializer = new FlowAlgorithmConfigurationSerializer();\n" + 
				"		try (ByteArrayOutputStream serializerOutput = new ByteArrayOutputStream()) {\n" + 
				"			serializer.write(flowConf, serializerOutput);\n" + 
				"			byte[] configuratorData = serializerOutput.toByteArray();\n" + 
				"			flow.setRaw(ByteBuffer.wrap(configuratorData));\n" + 
				"		} catch (AlgorithmConfigurationSerializerException | IOException e) {\n" + 
				"			e.printStackTrace();\n" + 
				"		}\n" + 
				"		return flow;\n" + 
				"	}\n" + 
				"\n" + 
				"	/**\n" + 
				"	 * Cria os dados de algoritmo a partir do configurador do algoritmo.\n" + 
				"	 * \n" + 
				"	 * @param algoConf\n" + 
				"	 *            configurador do algoritmo\n" + 
				"	 * @return os dados do algoritmo para a mensagem.\n" + 
				"	 */\n" + 
				"	public static Algorithm createAlgorithmFromConfig(AlgorithmConfigurator algoConf) {\n" + 
				"		Algorithm algorithm = new Algorithm();\n" + 
				"		algorithm.setAlgorithmId(algoConf.getAlgorithmId());\n" + 
				"		algorithm.setAlgorithmVersion(algoConf.getAlgorithmVersion().toString());\n" + 
				"		algorithm.setAlgorithmName(algoConf.getAlgorithmName());\n" + 
				"\n" + 
				"		List<AlgorithmParameter> params = ((SimpleAlgorithmConfigurator) algoConf).getSimpleParameters().stream()\n" + 
				"                                                                              .map(CSBaseCommandUtil::convertFromCSBaseParameter).collect(Collectors.toList());\n" + 
				"		algorithm.setParameters(params);\n" + 
				"		return algorithm;\n" + 
				"	}\n" + 
				"\n" + 
				"	/**\n" + 
				"	 * Faz o mapeamento entre a enumeração de status de finalização de comando do\n" + 
				"	 * schema e do csbase.\n" + 
				"	 *\n" + 
				"	 * @param finalizationInfo\n" + 
				"	 *            informações de finalização do comando\n" + 
				"	 * @return o status de finalização do comando no schema.\n" + 
				"	 */\n" + 
				"	public static ExitStatus getExitStatusFromInfo(CommandFinalizationInfo finalizationInfo) {\n" + 
				"		switch (finalizationInfo.getFinalizationType()) {\n" + 
				"		case SUCCESS:\n" + 
				"			return ExitStatus.SUCCESS;\n" + 
				"		case LOST:\n" + 
				"			return ExitStatus.LOST;\n" + 
				"		case EXECUTION_ERROR:\n" + 
				"			return ExitStatus.EXECUTION_ERROR;\n" + 
				"		case KILLED:\n" + 
				"			return ExitStatus.KILLED;\n" + 
				"		case FAILED:\n" + 
				"			switch (finalizationInfo.getFailureCause()) {\n" + 
				"			case UNKNOWN:\n" + 
				"				return ExitStatus.UNKNOWN;\n" + 
				"			case COMMAND_IDENTIFIER_NOT_FOUND:\n" + 
				"				return ExitStatus.COMMAND_IDENTIFIER_NOT_FOUND;\n" + 
				"			case SGA_EXECUTION_ERROR:\n" + 
				"				return ExitStatus.UNEXPECTED_MACHINE_ERROR;\n" + 
				"			case FAILED_SETUP_EXECUTION_ENVIRONMENT:\n" + 
				"				return ExitStatus.FAILED_SETUP_EXECUTION_ENVIRONMENT;\n" + 
				"			case PROJECT_NOT_FOUND:\n" + 
				"				return ExitStatus.PROJECT_NOT_FOUND;\n" + 
				"			case NO_SGA_AVAILABLE_TO_ROOT_COMMAND:\n" + 
				"			case SGA_IS_NOT_AVAILABLE:\n" + 
				"				return ExitStatus.NO_MACHINE_AVAILABLE;\n" + 
				"			case USER_WITHOUT_PERMISSION_FOR_EXECUTION:\n" + 
				"				return ExitStatus.NO_PERMISSION;\n" + 
				"			default:\n" + 
				"				return ExitStatus.UNDEFINED;\n" + 
				"			}\n" + 
				"		case NO_EXIT_CODE:\n" + 
				"		case END:\n" + 
				"		case UNKNOWN:\n" + 
				"			return ExitStatus.UNKNOWN;\n" + 
				"		default:\n" + 
				"			return ExitStatus.UNDEFINED;\n" + 
				"		}\n" + 
				"	}\n" + 
				"\n" + 
				"\n" + 
				"	public static ICommandInfo buildIcommandInfo(CommandInfo command){\n" + 
				"		return new ICommandInfo() {\n" + 
				"			@Override\n" + 
				"			public String getCommandId() {\n" + 
				"				return command.getId();\n" + 
				"			}\n" + 
				"\n" + 
				"			@Override\n" + 
				"			public String getDescription() {\n" + 
				"				return command.getDescription();\n" + 
				"			}\n" + 
				"\n" + 
				"			@Override\n" + 
				"			public String getProjectId() {\n" + 
				"				return (String) command.getProjectId();\n" + 
				"			}\n" + 
				"\n" + 
				"			@Override\n" + 
				"			public CommandStatus getStatus() {\n" + 
				"				switch (command.getStatus()) {\n" + 
				"					case DOWNLOADING:\n" + 
				"						return CommandStatus.DOWNLOADING;\n" + 
				"					case EXECUTING:\n" + 
				"						return CommandStatus.EXECUTING;\n" + 
				"					case FINISHED:\n" + 
				"						return CommandStatus.FINISHED;\n" + 
				"					case INIT:\n" + 
				"						return CommandStatus.INIT;\n" + 
				"					case SCHEDULED:\n" + 
				"						if(command.getSubmissionAttempts() > 1) {\n" + 
				"							return CommandStatus.RESCHEDULED;\n" + 
				"						}\n" + 
				"						return CommandStatus.SCHEDULED;\n" + 
				"					case SYSTEM_FAILURE:\n" + 
				"						return CommandStatus.SYSTEM_FAILURE;\n" + 
				"					case UPLOADING:\n" + 
				"						return CommandStatus.UPLOADING;\n" + 
				"					default:\n" + 
				"						throw new IllegalArgumentException(\n" + 
				"							\"Tipo do evento inválido: \" + command.getStatus());\n" + 
				"				}\n" + 
				"			}\n" + 
				"\n" + 
				"			@Override\n" + 
				"			public FinalizationType getFinalizationType() {\n" + 
				"				CommandFinalizationInfo finalizationInfo = command.getFinalizationInfo();\n" + 
				"				CommandFinalizationType type = finalizationInfo.getFinalizationType();\n" + 
				"				switch (type) {\n" + 
				"					case END:\n" + 
				"						return FinalizationType.COMPLETED;\n" + 
				"					case EXECUTION_ERROR:\n" + 
				"						return FinalizationType.ERROR;\n" + 
				"					case FAILED:\n" + 
				"						return FinalizationType.INIT_FAILURE;\n" + 
				"					case KILLED:\n" + 
				"						return FinalizationType.KILLED;\n" + 
				"					case LOST:\n" + 
				"						return FinalizationType.LOST;\n" + 
				"					case NO_EXIT_CODE:\n" + 
				"						return FinalizationType.NO_CODE;\n" + 
				"					case SUCCESS:\n" + 
				"						return FinalizationType.SUCCESS;\n" + 
				"					default:\n" + 
				"						throw new IllegalArgumentException(\"Tipo do evento inválido: \" + type);\n" + 
				"				}\n" + 
				"			}\n" + 
				"\n" + 
				"			@Override\n" + 
				"			public Integer getExitCode() {\n" + 
				"				CommandFinalizationInfo finalizationInfo = command.getFinalizationInfo();\n" + 
				"				return finalizationInfo.getExitCode();\n" + 
				"			}\n" + 
				"\n" + 
				"			@Override\n" + 
				"			public Integer getGuiltyNodeId() {\n" + 
				"				CommandFinalizationInfo finalizationInfo = command.getFinalizationInfo();\n" + 
				"				CommandFinalizationInfo.FinalizationInfoType infoType = finalizationInfo.getInfoType();\n" + 
				"				if (infoType.equals(CommandFinalizationInfo.FinalizationInfoType.EXTENDED)) {\n" + 
				"					ExtendedCommandFinalizationInfo extendedInfo =\n" + 
				"						(ExtendedCommandFinalizationInfo) finalizationInfo;\n" + 
				"					return extendedInfo.getGuiltyNodeId();\n" + 
				"				}\n" + 
				"				return null;\n" + 
				"			}\n" + 
				"		};\n" + 
				"	}\n" + 
				"\n" + 
				"\n" + 
				"}";
		InjectCode injectCode = new InjectCode();
		String result = injectCode.injectCodeMethods(code.toCharArray(), "log", "feature");
		System.out.println(result);
	}
	
	//@Test
	public void testTwoMethods() {
		//String code = "public void name1() { return a; } public void name2() { str = \"}\";block; } public void name3(int a, int b) { return a + b; }   public void name4(int a, int x) {return a;} ";
		//String code = "public void name() { int a; int b; } public void name1() {  try { return a; } \n catch (Exception e) { return b; } \n }";
		String code = "@Path(\"/authentication\")\n" + 
				"@Consumes({ \"application/x-www-form-urlencoded\" })\n" + 
				"@Produces({ \"application/json;\" })\n" + 
				"@io.swagger.annotations.Api(description = \"the authentication API\")\n" + 
				"@javax.annotation.Generated(value = \"class io.swagger.codegen.languages.JavaJerseyServerCodegen\", date = \"2019-12-06T18:10:35.146-02:00\")\n" + 
				"public class AuthenticationApi  {\n" + 
				"   private final AuthenticationApiService delegate = AuthenticationApiServiceFactory.getAuthenticationApi();\n" + 
				"\n" + 
				"    @POST\n" + 
				"    @Path(\"/facebook\")\n" + 
				"    @Consumes({ \"application/x-www-form-urlencoded\" })\n" + 
				"    @Produces({ \"application/json;\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Generate an access token for facebook user access\", notes = \"\", response = Token.class, tags={ \"Authentication\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"A JSON-encoded dictionary including an access token (accessToken), token type (tokenType), and user ID. The token type will always be \\\"bearer\\\".\", response = Token.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 400, message = \"If the facebook access token was not provided (or is empty).\", response = Token.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 401, message = \"Invalid facebook user token.\", response = Token.class) })\n" + 
				"    public Response authenticationFacebookPost(@ApiParam(value = \"the facebook user's token\", required=true)  @FormParam(\"accessToken\")  String accessToken\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\")  @FormParam(\"locale\")  String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.authenticationFacebookPost(accessToken,locale,securityContext);\n" + 
				"    }\n" + 
				"    @POST\n" + 
				"    @Path(\"/newpassword/accept\")\n" + 
				"    @Consumes({ \"application/x-www-form-urlencoded\" })\n" + 
				"    @Produces({ \"application/json;\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Replace the current password for a new one.\", notes = \"\", response = Token.class, tags={ \"Authentication\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"A JSON-encoded dictionary including an access token (accessToken), token type (tokenType), and user ID. The token type will always be \\\"bearer\\\".\", response = Token.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 400, message = \"If the user token was not provided (or is empty) or if there is no user with login provided\", response = Token.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 401, message = \"Invalid user token.\", response = Token.class) })\n" + 
				"    public Response authenticationNewpasswordAcceptPost(@ApiParam(value = \"the user's token sent by the validation process.\", required=true)  @FormParam(\"userToken\")  String userToken\n" + 
				",@ApiParam(value = \"The new password to be replaced.\", required=true)  @FormParam(\"password\")  String password\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\")  @FormParam(\"locale\")  String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.authenticationNewpasswordAcceptPost(userToken,password,locale,securityContext);\n" + 
				"    }\n" + 
				"    @POST\n" + 
				"    @Path(\"/newpassword/validation\")\n" + 
				"    @Consumes({ \"application/x-www-form-urlencoded\" })\n" + 
				"    @Produces({ \"application/json;\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"A token is created and sent by email, embedded in the url link used to validate the new password.\", notes = \"\", response = void.class, tags={ \"Authentication\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"A token is created and sent by the email provided\", response = void.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 400, message = \"If there is no user with login equals the email provided\", response = void.class) })\n" + 
				"    public Response authenticationNewpasswordValidationPost(@ApiParam(value = \"The email of the user (used also as the user login).\", required=true)  @FormParam(\"email\")  String email\n" + 
				",@ApiParam(value = \"The base URL to be used for build the link sent by email for the validation process\", required=true)  @FormParam(\"baseURL\")  String baseURL\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\")  @FormParam(\"locale\")  String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.authenticationNewpasswordValidationPost(email,baseURL,locale,securityContext);\n" + 
				"    }\n" + 
				"    @POST\n" + 
				"    @Path(\"/newuser/accept\")\n" + 
				"    @Consumes({ \"application/x-www-form-urlencoded\" })\n" + 
				"    @Produces({ \"application/json;\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Generate an access token for new user access. The new user is also added to the system, as a guest.\", notes = \"\", response = Token.class, tags={ \"Authentication\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"A JSON-encoded dictionary including an access token (accessToken), token type (tokenType), and user ID. The token type will always be \\\"bearer\\\".\", response = Token.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 400, message = \"If the access token was not provided (or is empty) or if there is already an user with login provided\", response = Token.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 401, message = \"Invalid user token.\", response = Token.class) })\n" + 
				"    public Response authenticationNewuserAcceptPost(@ApiParam(value = \"the user's token wich embeds the required information to create a new guest user.\", required=true)  @FormParam(\"userToken\")  String userToken\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\")  @FormParam(\"locale\")  String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.authenticationNewuserAcceptPost(userToken,locale,securityContext);\n" + 
				"    }\n" + 
				"    @POST\n" + 
				"    @Path(\"/newuser/validation\")\n" + 
				"    @Consumes({ \"application/x-www-form-urlencoded\" })\n" + 
				"    @Produces({ \"application/json;\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Create a token to be used to validate the email provided by the user. The token is sent by email, embedded in the url link used to validate the access.\", notes = \"\", response = void.class, tags={ \"Authentication\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"A token is created and sent to the provided email.\", response = void.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 400, message = \"If an user already exists with a login equal to the provided email.\", response = void.class) })\n" + 
				"    public Response authenticationNewuserValidationPost(@ApiParam(value = \"The name of the user.\", required=true)  @FormParam(\"name\")  String name\n" + 
				",@ApiParam(value = \"The email of the user. The email is used as the user login.\", required=true)  @FormParam(\"email\")  String email\n" + 
				",@ApiParam(value = \"The password of the user.\", required=true)  @FormParam(\"password\")  String password\n" + 
				",@ApiParam(value = \"The base URL to be used for building the link sent by email for the validation process.\", required=true)  @FormParam(\"baseURL\")  String baseURL\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\")  @FormParam(\"locale\")  String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.authenticationNewuserValidationPost(name,email,password,baseURL,locale,securityContext);\n" + 
				"    }\n" + 
				"    @POST\n" + 
				"    \n" + 
				"    @Consumes({ \"application/x-www-form-urlencoded\" })\n" + 
				"    @Produces({ \"application/json;\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Generate an access token for login and password\", notes = \"\", response = Token.class, tags={ \"Authentication\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"A JSON-encoded dictionary including an access token (accessToken), token type (tokenType), and user ID. The token type will always be \\\"bearer\\\".\", response = Token.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 400, message = \"If login or password are not provided (or are empty values).\", response = Token.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 401, message = \"Invalid login/password\", response = Token.class) })\n" + 
				"    public Response authenticationPost(@ApiParam(value = \"the user's login\", required=true)  @FormParam(\"login\")  String login\n" + 
				",@ApiParam(value = \"the user's password\", required=true)  @FormParam(\"password\")  String password\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\")  @FormParam(\"locale\")  String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.authenticationPost(login,password,locale,securityContext);\n" + 
				"    }\n" + 
				"    @POST\n" + 
				"    @Path(\"/renew\")\n" + 
				"    @Consumes({ \"application/x-www-form-urlencoded\" })\n" + 
				"    @Produces({ \"application/json;\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Generate a new access token based on an old\", notes = \"\", response = Token.class, tags={ \"Authentication\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"A JSON-encoded dictionary including an access token (accessToken), token type (tokenType), and user ID. The token type will always be \\\"bearer\\\".\", response = Token.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 400, message = \"if expiration time is greater than original token\", response = Token.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 401, message = \"Invalid or Unauthorized token\", response = Token.class) })\n" + 
				"    public Response authenticationRenewPost(@ApiParam(value = \"Old token\", required=true)  @FormParam(\"token\")  String token\n" + 
				",@ApiParam(value = \"Expiration time in milliseconds. If empty, the expiration time will be the same as the original token\")  @FormParam(\"expiration\")  Long expiration\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\")  @FormParam(\"locale\")  String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.authenticationRenewPost(token,expiration,locale,securityContext);\n" + 
				"    }\n" + 
				"    @POST\n" + 
				"    @Path(\"/token/validation\")\n" + 
				"    @Consumes({ \"application/x-www-form-urlencoded\" })\n" + 
				"    @Produces({ \"application/json;\" })\n" + 
				"    @io.swagger.annotations.ApiOperation(value = \"Check if a token is valid. If the token type is \\\"new_user\\\" and the user already exists, the token is invalid. If the token type is \\\"new_password\\\" or \\\"access\\\" and the user was not found or if the token was issued after the last update of the user data, the token is invalid.\", notes = \"\", response = TokenValidation.class, tags={ \"Authentication\", })\n" + 
				"    @io.swagger.annotations.ApiResponses(value = { \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 200, message = \"The result of token validation.\", response = TokenValidation.class),\n" + 
				"        \n" + 
				"        @io.swagger.annotations.ApiResponse(code = 400, message = \"If the token type or the user token was not provided (or is empty)\", response = TokenValidation.class) })\n" + 
				"    public Response authenticationTokenValidationPost(@ApiParam(value = \"the user's token to be validate.\", required=true)  @FormParam(\"userToken\")  String userToken\n" + 
				",@ApiParam(value = \"\", allowableValues=\"new_user, new_password, access\")  @FormParam(\"validationType\")  String validationType\n" + 
				",@ApiParam(value = \"The locale adopted for internationalization. When provided, this locale defines the language for message responses.\")  @FormParam(\"locale\")  String locale\n" + 
				",@Context SecurityContext securityContext)\n" + 
				"    throws NotFoundException {\n" + 
				"        return delegate.authenticationTokenValidationPost(userToken,validationType,locale,securityContext);\n" + 
				"    }\n" + 
				"}";
		System.out.println(code);
		InjectCode injectCode = new InjectCode();
		String result = injectCode.injectCodeMethods(code.toCharArray(), "log", "feature");
		System.out.println(result);
	}
	
	//@Test
	public void testOneMethod() {
		String javaFolder = "/home/luizmatheus/tecgraf/rest-services";
		InjectCode injectCode = new InjectCode();
		boolean result = injectCode.injectCodeInAllFiles(new File(javaFolder), 
				"/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/log", 
				"/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/feature");
	}
	
	@Test
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
