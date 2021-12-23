package com.hell.action.common;

import com.hell.dto.request.TokenRequest;
import com.hell.dto.response.TokenResponse;
import com.hell.config.action.BaseAction;
import com.hell.security.token.ResubmitTokenManager;
import com.hell.security.token.Token;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "common")
public class TokenAction implements BaseAction<TokenRequest, TokenResponse> {

    @Resource
    ResubmitTokenManager resubmitTokenManager;

    @ApiOperation("token生成")
    @GetMapping(path = "/api/common/token")
    @Override
    public TokenResponse execute(HttpServletRequest request, @Valid TokenRequest request2) throws Exception {
        Token token = resubmitTokenManager.createToken(request);
        TokenResponse r = new TokenResponse();
        r.set_tokenName(token.getUniqueId());
        return r;
    }
}
