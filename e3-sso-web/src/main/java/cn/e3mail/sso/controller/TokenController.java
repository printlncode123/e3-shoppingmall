package cn.e3mail.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.jute.compiler.generated.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mail.sso.service.TokenService;
import cn.e3mail.utils.E3Result;
import cn.e3mail.utils.JsonUtils;

@Controller
public class TokenController {
	@Autowired
	private TokenService tokenService;
	/*@RequestMapping(value="/user/token/{token}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getToken(@PathVariable String token,String callback){
		E3Result result = tokenService.getToken(token);
		if (StringUtils.isNotBlank(callback)) {
			return callback+"("+JsonUtils.objectToJson(result)+");";
		}
		return JsonUtils.objectToJson(result);
	}
*/
	@RequestMapping(value="/user/token/{token}")
	public Object getToken(@PathVariable String token,String callback){
		E3Result result = tokenService.getToken(token);
		if (StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mappingJacksonValue=new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}
	
}
