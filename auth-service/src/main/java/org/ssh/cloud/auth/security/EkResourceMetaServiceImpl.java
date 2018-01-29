package org.ssh.cloud.auth.security;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.utils.mapper.JsonMapper;
import org.ssh.boot.security.ResourceMetaService;
import org.ssh.boot.security.dto.AuthoritiesDto;
import org.ssh.boot.utils.cache.ICache;
import org.ssh.cloud.auth.mapper.RoleResourceMapper;

@Slf4j
public class EkResourceMetaServiceImpl implements ResourceMetaService {

  @Autowired
  RoleResourceMapper roleResourceMapper;
  @Autowired
  ICache cache;

  @Override
  public Map<String, List<AuthoritiesDto>> getAuthorities() {
    String key = "EK_AUTHORITIES";
    String jsonString = cache.getValue(key);

    Map<String, List<AuthoritiesDto>> authorities = null;
    if (StringUtils.isBlank(jsonString)) {
      List<AuthoritiesDto> authoritiesDto = roleResourceMapper.findAllAuthorities();

      authorities = authoritiesDto
        .stream().collect(groupingBy(AuthoritiesDto::getUrl, toList()));

      log.info("getAuthorities from db: {}", authorities);

      jsonString = JsonMapper.INSTANCE.toJson(authorities);
      cache.setValue(key, jsonString);

    } else {
      TypeReference<HashMap<String, List<AuthoritiesDto>>> personList =
        new TypeReference<HashMap<String, List<AuthoritiesDto>>>() {
        };
      try {
        authorities = JsonMapper.INSTANCE.getMapper().readValue(jsonString, personList);
      } catch (IOException e) {
        log.error("{}", e);
      }

    }
    log.info("getAuthorities: {}", authorities);
    return authorities;
  }
}
