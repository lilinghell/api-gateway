package com.hell.service;

import com.hell.common.CheckMsg;
import com.hell.common.Dictionary;
import com.hell.dao.SAppDao;
import com.hell.db.table.provider.SApi;
import com.hell.db.table.provider.SAppEnv;
import com.hell.entity.api.ApiParameter;
import com.hell.entity.api.ApiTable;
import com.hell.core.exception.ValidationException;
import io.swagger.models.*;
import io.swagger.models.parameters.*;
import io.swagger.models.properties.*;
import io.swagger.parser.SwaggerParser;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiTableServiceImpl implements ApiTableService {

    @Value("${diga.gateway.url:http://localhost:8080}")
    private String gatewayUrl;
    private SAppDao appDao;

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final String carve = "&nbsp;&nbsp;&nbsp;&nbsp;";

    public List<ApiTable> getApiTableList(SApi api, SAppEnv appEnv) throws ValidationException {
        List<ApiTable> list = new ArrayList<>();

//        SApi api = apiDao.findBySeqAndEntSeq(apiSeq, entSeq);


        ApiTable apiTable = new ApiTable();
        apiTable.setApiName(api.getName());
        apiTable.setUrl(api.getUrl());
        apiTable.setVersion(api.getVersion());
        apiTable.setDescription(api.getDetail());
        apiTable.setMethod(api.getMethod());
        if (appEnv.getServiceType().equals(Dictionary.SERVICE_TYPE_1)) {
            apiTable.setServiceId(api.getServiceId());
        }

        Operation operation = getOperation(api);
        List<String> a = new ArrayList<>();
        a.add("application/json");
        apiTable.setProduces(operation.getProduces() == null ? a : operation.getProduces());
        apiTable.setConsumes(operation.getConsumes() == null ? a : operation.getConsumes());
        //请求参数
        List<Parameter> parameters = operation.getParameters();
        List<ApiParameter> requests = new ArrayList<>();
        for (Parameter para : parameters) {
            String in = para.getIn();
            if ("body".equals(in)) {
                getBodyParameter(para, requests, api);
            }
            if ("query".equals(in)) {
                getQueryParameter(para, requests, api);
            }
            if ("path".equals(in)) {
                getPathParameter(para, requests);
            }
            if ("formData".equals(in)) {
                getFormDataParameter(para, requests);
            }
            if ("header".equals(in)) {
                getHeadParameter(para, requests);
            }
        }
        apiTable.setParametersList(requests);
        //应答
        Map<String, Response> responses = operation.getResponses();
        Response response = responses.get("200");
        List<ApiParameter> res = new ArrayList<>();
        getResponse(response, res, api);
        apiTable.setResponsesList(res);
        //请求示例
        List<String> requestDemo = getRequestDemo(apiTable, appEnv);
        apiTable.setRequestDemo(requestDemo);
        Map<String, Object> requestObject = getRequestObject(apiTable);
        apiTable.setRequestObjectDemo(requestObject);

        //应答示例
        Map<String, Object> responseObject = getResponseObject(apiTable);
        apiTable.setResponseDemo(responseObject);

        list.add(apiTable);
        return list;
    }

    private Map<String, Object> getResponseObject(ApiTable apiTable) {
        List<ApiParameter> responses = apiTable.getResponsesList();
        return objectDemo(responses);
    }

    private void getHeadParameter(Parameter para, List<ApiParameter> requests) {
        HeaderParameter headerParameter = (HeaderParameter) para;

        ApiParameter apiParameter = new ApiParameter();
        apiParameter.setIn("header");
        apiParameter.setName(headerParameter.getName());
        apiParameter.setType(headerParameter.getType());
        apiParameter.setFormat((headerParameter.getFormat() == null || headerParameter.getFormat().equals("none")) ? ""
                : headerParameter.getFormat());
        apiParameter.setRequired(headerParameter.getRequired());
        apiParameter.setDescription(headerParameter.getDescription());
        apiParameter.setOtherInfo(new HashMap<>());
        requests.add(apiParameter);
    }

    private Map<String, Object> getRequestObject(ApiTable apiTable) {
        List<ApiParameter> request = apiTable.getParametersList();
        List<ApiParameter> bodyRequest = new ArrayList<>();
        for (ApiParameter api : request) {
            if ("body".equals(api.getIn())) {
                bodyRequest.add(api);
            }
        }
        return objectDemo(bodyRequest);
    }

    private List<String> getRequestDemo(ApiTable apiTable, SAppEnv appEnv) {
        List<ApiParameter> request = apiTable.getParametersList();
        List<ApiParameter> queryRequest = new ArrayList<>();
        List<ApiParameter> pathRequest = new ArrayList<>();
        List<ApiParameter> headRequest = new ArrayList<>();
        List<ApiParameter> formDataRequest = new ArrayList<>();
        for (ApiParameter api : request) {
            if ("query".equals(api.getIn())) {
                queryRequest.add(api);
            }
            if ("path".equals(api.getIn())) {
                pathRequest.add(api);
            }
            if ("header".equals(api.getIn())) {
                headRequest.add(api);
            }
            if ("formData".equals(api.getIn())) {
                formDataRequest.add(api);
            }
        }
        List<String> reList = new ArrayList<>();

        StringBuffer sb = new StringBuffer("curl -X ");
        sb.append("'" + apiTable.getMethod() + "' ");
        reList.add(sb.toString());

        pathAndQueryDemo(pathRequest, queryRequest, apiTable, reList);

        headDemo(headRequest, reList, apiTable, appEnv);

        formDataDemo(formDataRequest, reList);

        return reList;
    }

    private void formDataDemo(List<ApiParameter> formDataRequest, List<String> reList) {
        for (int i = 0; i < formDataRequest.size(); i++) {
            ApiParameter apiParameter = formDataRequest.get(i);
            reList.add(" -F '" + apiParameter.getName() + "=" + getTypeDemoValue(apiParameter.getType()) + "'");
        }
    }

    private void headDemo(List<ApiParameter> headRequest, List<String> reList, ApiTable apiTable, SAppEnv appEnv) {

        reList.add(" -H 'accept: " + apiTable.getConsumes().get(0) + "' ");
        reList.add(" -H 'Content-Type: " + apiTable.getProduces().get(0) + "' ");
        reList.add(" -H 'X-Request-EnvKey: @{X-Request-EnvKey}' ");
        for (int i = 0; i < headRequest.size(); i++) {
            ApiParameter apiParameter = headRequest.get(i);
            reList.add(" -H '" + apiParameter.getName() + ": " + getTypeDemoValue(apiParameter.getType()) + "'");
        }
    }

    private Map<String, Object> objectDemo(List<ApiParameter> bodyRequest) {
        Map<String, Object> body = new HashMap<>();
        for (int i = 0; i < bodyRequest.size(); i++) {
            ApiParameter apiParameter = bodyRequest.get(i);
            if (apiParameter.getName().indexOf(carve) == -1) {
                if ("array".equals(apiParameter.getType())) {
                    body.put(apiParameter.getName(), getArraySubApi(apiParameter, bodyRequest, i + 1, 1));
                } else if ("ref".equals(apiParameter.getType()) || "object".equals(apiParameter.getType())) {
                    body.put(apiParameter.getName(), getObjectSubApi(apiParameter, bodyRequest, i + 1, 1));
                } else {
                    body.put(apiParameter.getName(), getTypeDemoValue(apiParameter.getType()));
                }
            }
        }
        return body;
    }

    private Object getTypeDemoValue(String type) {
        Object value = type;
        if ("integer".equals(type)) {
            value = 1;
        }
        if ("string".equals(type)) {
            value = "string";
        }
        if ("boolean".equals(type)) {
            value = true;
        }
        if ("number".equals(type)) {
            value = 1.0;
        }
        if ("file".equals(type)) {
            value = "@xxx.png;type=image/png";
        }
        return value;
    }

    private void pathAndQueryDemo(List<ApiParameter> pathRequest, List<ApiParameter> queryRequest,
                                  ApiTable apiTable, List<String> list) {
        StringBuffer sb = new StringBuffer(" ");
        String url = apiTable.getUrl();
        for (int i = 0; i < pathRequest.size(); i++) {
            ApiParameter apiParameter = pathRequest.get(i);
            String name = apiParameter.getName();

            Object value = getTypeDemoValue(apiParameter.getType());
            url = url.replaceAll("\\{" + name + "\\}", String.valueOf(value));
        }
        StringBuffer urlBuffer = new StringBuffer(url);
        for (int i = 0; i < queryRequest.size(); i++) {
            if (i == 0) {
                urlBuffer.append("?");
            }
            ApiParameter apiParameter = queryRequest.get(i);
            urlBuffer.append(apiParameter.getName());
            urlBuffer.append("=");
            urlBuffer.append(getTypeDemoValue(apiParameter.getType()));
            urlBuffer.append("&");
        }
        url = urlBuffer.toString();
        if (StringUtils.endsWith(url, "&")) {
            url = url.substring(0, url.length() - 1);
        }
        String rootPath = gatewayUrl;
        if (StringUtils.isNotEmpty(apiTable.getServiceId())) {
            //微服务
            rootPath = rootPath + "/" + apiTable.getServiceId();
        }
        sb.append("'" + rootPath + url + "' ");
        list.add(sb.toString());
    }

    private List<?> getArraySubApi(ApiParameter currentApiParameter, List<ApiParameter> request, int startIndex, int level) {
        Map<String, Object> ot = currentApiParameter.getOtherInfo();
        if (ObjectUtils.isNotEmpty(ot)) {
            List<String> list2 = new ArrayList<>();
            list2.add(String.valueOf(getTypeDemoValue(String.valueOf(ot.get("type")))));
            return list2;
        } else {
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> d = getObjectSubApi(currentApiParameter, request, startIndex, level);
            list.add(d);
            return list;
        }
    }

    private Map<String, Object> getObjectSubApi(ApiParameter currentApiParameter, List<ApiParameter> request, int startIndex, int level) {
        Map<String, Object> d = new HashMap<>();
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < level; i++) {
            s.append(carve);
        }
        for (int i = startIndex; i < request.size(); i++) {
            ApiParameter subApi = request.get(i);
            String subName = subApi.getName();
            if (StringUtils.countMatches(subName, carve) == level) {
                if ("array".equals(subApi.getType())) {
                    d.put(StringUtils.removeStart(subName, s.toString()), getArraySubApi(subApi, request, i + 1, level + 1));
                } else if ("ref".equals(subApi.getType())) {
                    d.put(StringUtils.removeStart(subName, s.toString()), getObjectSubApi(subApi, request, i + 1, level + 1));
                } else {
                    d.put(StringUtils.removeStart(subName, s.toString()), getTypeDemoValue(subApi.getType()));
                }
            } else {
                break;
            }
        }
        return d;
    }

    private void getFormDataParameter(Parameter para, List<ApiParameter> requests) {
        try {
            FormParameter formParameter = (FormParameter) para;
            ApiParameter apiParameter = new ApiParameter();

            apiParameter.setName(formParameter.getName());
            apiParameter.setType(formParameter.getType());
            apiParameter.setDescription(formParameter.getDescription());
            apiParameter.setRequired(formParameter.getRequired());
            apiParameter.setFormat((formParameter.getFormat() == null || formParameter.getFormat().equals("none")) ? "" : formParameter.getFormat());
            apiParameter.setIn("formData");
            Map<String, Object> map = new HashMap<>();
            if ("string".equals(formParameter.getType())) {
                if (ObjectUtils.isNotEmpty(formParameter.getMinLength())) {
                    map.put("miniLength", formParameter.getMinLength());
                }
                if (ObjectUtils.isNotEmpty(formParameter.getMaxLength())) {
                    map.put("maxLength", formParameter.getMaxLength());
                }
                if (ObjectUtils.isNotEmpty(formParameter.getDefault())) {
                    map.put("defaultValue", formParameter.getDefault());
                }
                if (ObjectUtils.isNotEmpty(formParameter.getEnum())) {
                    map.put("enumValue", formParameter.getEnum());
                }
            }
            if ("integer".equals(formParameter.getType())) {
                if (ObjectUtils.isNotEmpty(formParameter.getMinimum())) {
                    map.put("minimum", formParameter.getMinimum());
                }
                if (ObjectUtils.isNotEmpty(formParameter.getMaximum())) {
                    map.put("maximum", formParameter.getMaximum());
                }
                if (ObjectUtils.isNotEmpty(formParameter.getEnum())) {
                    map.put("enumValue", formParameter.getEnum());
                }
            }
            apiParameter.setOtherInfo(map);

            requests.add(apiParameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPathParameter(Parameter para, List<ApiParameter> requests) {
        try {
            PathParameter pathParameter = (PathParameter) para;
            ApiParameter apiParameter = new ApiParameter();
            apiParameter.setIn("path");
            apiParameter.setType(pathParameter.getType());
            apiParameter.setName(pathParameter.getName());
            apiParameter.setDescription(pathParameter.getDescription());
            apiParameter.setRequired(pathParameter.getRequired());
            apiParameter.setFormat(pathParameter.getFormat());
            Map<String, Object> map = new HashMap<>();
            if ("string".equals(pathParameter.getType())) {
                if (ObjectUtils.isNotEmpty(pathParameter.getMinLength())) {
                    map.put("miniLength", pathParameter.getMinLength());
                }
                if (ObjectUtils.isNotEmpty(pathParameter.getMaxLength())) {
                    map.put("maxLength", pathParameter.getMaxLength());
                }
                if (ObjectUtils.isNotEmpty(pathParameter.getDefault())) {
                    map.put("defaultValue", pathParameter.getDefault());
                }
                if (ObjectUtils.isNotEmpty(pathParameter.getEnum())) {
                    map.put("enumValue", pathParameter.getEnum());
                }
            }
            if ("integer".equals(pathParameter.getType())) {
                if (ObjectUtils.isNotEmpty(pathParameter.getMinimum())) {
                    map.put("minimum", pathParameter.getMinimum());
                }
                if (ObjectUtils.isNotEmpty(pathParameter.getMaximum())) {
                    map.put("maximum", pathParameter.getMaximum());
                }
                if (ObjectUtils.isNotEmpty(pathParameter.getEnum())) {
                    map.put("enumValue", pathParameter.getEnum());
                }
            }
            apiParameter.setOtherInfo(map);
            requests.add(apiParameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getQueryParameter(Parameter para, List<ApiParameter> requests, SApi api) {
        QueryParameter queryParameter = (QueryParameter) para;

        ApiParameter apiParameter = new ApiParameter();
        apiParameter.setType(queryParameter.getType());
        apiParameter.setDescription(queryParameter.getDescription());
        apiParameter.setRequired(queryParameter.getRequired());
        apiParameter.setName(queryParameter.getName());
        apiParameter.setIn("query");
        apiParameter.setFormat((queryParameter.getFormat() == null || queryParameter.getFormat().equals("none")) ? "" : queryParameter.getFormat());
        apiParameter.setOtherInfo(new HashMap<>());
        if ("array".equals(queryParameter.getType())) {
            Property property = queryParameter.getItems();
            Swagger swagger = parseAndPrepareSwagger(api.getInfo());
            getOtherInfoProperties(requests, swagger, apiParameter, property, 1);
        }
        requests.add(apiParameter);
    }

    private void getResponse(Response response, List<ApiParameter> responses, SApi api) {
        Model model = response.getResponseSchema();
        List<ApiParameter> list = getParametersList(model, api);
        responses.addAll(list);
    }

    private List<ApiParameter> getRefParameter(SApi api, RefModel model) {
        Swagger swagger = parseAndPrepareSwagger(api.getInfo());
        List<ApiParameter> list = new ArrayList<>();
        getRefParameter(list, swagger, model.getSimpleRef(), 1);
        return list;
    }

    private void getBodyParameter(Parameter para, List<ApiParameter> list, SApi api) {
        BodyParameter bodyParameter = (BodyParameter) para;
        Model model = bodyParameter.getSchema();
        List<ApiParameter> list2 = getParametersList(model, api);
        list.addAll(list2);
    }

    private List<ApiParameter> getParametersList(Model model, SApi api) {
        List<ApiParameter> list = new ArrayList<>();
        if (model instanceof RefModel) {
            RefModel mo = (RefModel) model;
            list = getRefParameter(api, mo);
        }
        if (model instanceof ArrayModel) {
            ArrayModel mo = (ArrayModel) model;
            Property itemsProperty = mo.getItems();
            if (itemsProperty instanceof RefProperty) {
                RefProperty p = (RefProperty) itemsProperty;
                Swagger swagger = parseAndPrepareSwagger(api.getInfo());
                getRefParameter(list, swagger, p.getSimpleRef(), 1);
            }
        }
        if (model instanceof ModelImpl) {
            Map<String, Property> propertyMap = model.getProperties();
            Swagger swagger = parseAndPrepareSwagger(api.getInfo());
            getObjectParameter(list, swagger, propertyMap, 1);
        }
        return list;
    }

    private void getObjectParameter(List<ApiParameter> apiParaList, Swagger swagger, Map<String, Property> propertyMap, int level) {
        propertyMap.forEach((key, value) -> {
            ApiParameter apiParameter = new ApiParameter();
            apiParameter.setIn("body");
            String name = "";
            if (level > 1) {
                for (int i = 1; i <= level - 1; i++) {
                    name = name + carve;
                }
            }
            name = name + key;
            apiParameter.setName(name);
            apiParameter.setType(value.getType());
            apiParameter.setRequired(value.getRequired());
            apiParameter.setDescription(value.getDescription());
            apiParameter.setFormat((value.getFormat() == null || value.getFormat().equals("none")) ? "" : value.getFormat());
            boolean addFlag = getOtherInfoProperties(apiParaList, swagger, apiParameter, value, level);
            if (addFlag) {
                apiParaList.add(apiParameter);
            }
        });
    }

    private void getRefParameter(List<ApiParameter> apiParaList, Swagger swagger, String ref, int level) {
        if (level > 4) {
            log.error("=====level太多====");
            return;
        }
        Model model = swagger.getDefinitions().get(ref);
        Map<String, Property> propertyMap = model.getProperties();
        getObjectParameter(apiParaList, swagger, propertyMap, level);
    }


    private boolean getOtherInfoProperties(List<ApiParameter> apiParaList, Swagger swagger, ApiParameter apiParameter, Property value, int level) {
        boolean addFlag = true;
        try {
            Map<String, Object> map = new HashMap<>();
            if (value instanceof StringProperty) {
                StringProperty property = (StringProperty) value;
                if (ObjectUtils.isNotEmpty(property.getMinLength())) {
                    map.put("miniLength", property.getMinLength());
                }
                if (ObjectUtils.isNotEmpty(property.getMaxLength())) {
                    map.put("maxLength", property.getMaxLength());
                }
                if (ObjectUtils.isNotEmpty(property.getDefault())) {
                    map.put("defaultValue", property.getDefault());
                }
                if (ObjectUtils.isNotEmpty(property.getEnum())) {
                    map.put("enumValue", property.getEnum());
                }
            }
            if (value instanceof IntegerProperty) {
                IntegerProperty property = (IntegerProperty) value;
                if (ObjectUtils.isNotEmpty(property.getMinimum())) {
                    map.put("minimum", property.getMinimum());
                }
                if (ObjectUtils.isNotEmpty(property.getMaximum())) {
                    map.put("maximum", property.getMaximum());
                }
                if (ObjectUtils.isNotEmpty(property.getEnum())) {
                    map.put("enumValue", property.getEnum());
                }
            }

            if (value instanceof ArrayProperty) {
                ArrayProperty property = (ArrayProperty) value;
                Property itemsProperty = property.getItems();
                if (itemsProperty instanceof RefProperty) {
                    RefProperty p = (RefProperty) itemsProperty;
                    apiParaList.add(apiParameter);
                    addFlag = false;
                    getRefParameter(apiParaList, swagger, p.getSimpleRef(), level + 1);
                } else if (itemsProperty instanceof ObjectProperty) {
                    ObjectProperty op = (ObjectProperty) itemsProperty;
                    Map<String, Property> propertyMap = op.getProperties();
                    apiParaList.add(apiParameter);
                    addFlag = false;
                    getObjectParameter(apiParaList, swagger, propertyMap, level + 1);
                } else {
                    map.put("type", itemsProperty.getType());//数组里面存放的类型
                }
            }
            if (value instanceof RefProperty) {
                RefProperty p = (RefProperty) value;
                apiParaList.add(apiParameter);
                addFlag = false;
                getRefParameter(apiParaList, swagger, p.getSimpleRef(), level + 1);
            }
            if (value instanceof DecimalProperty) {
                DecimalProperty property = (DecimalProperty) value;
                if (ObjectUtils.isNotEmpty(property.getMinimum())) {
                    map.put("minimum", property.getMinimum());
                }
                if (ObjectUtils.isNotEmpty(property.getMaximum())) {
                    map.put("maximum", property.getMaximum());
                }
            }
            if (value instanceof BooleanProperty) {
                BooleanProperty property = (BooleanProperty) value;
                if (ObjectUtils.isNotEmpty(property.getDefault())) {
                    map.put("defaultValue", property.getDefault());
                }
            }
            if (value instanceof ObjectProperty) {
                ObjectProperty property = (ObjectProperty) value;
                Map<String, Property> propertyMap = property.getProperties();
                apiParaList.add(apiParameter);
                addFlag = false;
                getObjectParameter(apiParaList, swagger, propertyMap, level + 1);
            }
            apiParameter.setOtherInfo(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addFlag;
    }

    private Operation getOperation(SApi api) throws ValidationException {
        String info = api.getInfo();
        Swagger swagger = parseAndPrepareSwagger(info);

        Path path = swagger.getPath(api.getUrl());
        if (api.getMethod().equals("GET")) {
            return path.getGet();
        }
        if (api.getMethod().equals("POST")) {
            return path.getPost();
        }
        if (api.getMethod().equals("PUT")) {
            return path.getPut();
        }
        if (api.getMethod().equals("DELETE")) {
            return path.getDelete();
        }
        throw new ValidationException(CheckMsg.VALIDATION_FORMAT_ERROR);
    }

    private Swagger parseAndPrepareSwagger(String info) {
        Swagger swagger = new SwaggerParser().readWithInfo(info).getSwagger();
        return swagger;
    }

    @Autowired
    public void setAppDao(SAppDao appDao) {
        this.appDao = appDao;
    }
}
