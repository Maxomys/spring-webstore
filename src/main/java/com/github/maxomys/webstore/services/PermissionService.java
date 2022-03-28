package com.github.maxomys.webstore.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final MutableAclService aclService;

    public void addPermissionForCurrentUser(Class<?> type, Long id, Permission permission) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        addPermission(name, type, id, permission);
    }

    public void addPermission(String username, Class<?> type, Long id, Permission permission) {
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(type, id);
        Sid sid = new PrincipalSid(username);
        MutableAcl acl;
        try {
            acl = (MutableAcl) aclService.readAclById(objectIdentity);
        } catch (NotFoundException exception) {
            acl = aclService.createAcl(objectIdentity);
        }
        acl.insertAce(acl.getEntries().size(), permission, sid, true);
        aclService.updateAcl(acl);
    }

}
