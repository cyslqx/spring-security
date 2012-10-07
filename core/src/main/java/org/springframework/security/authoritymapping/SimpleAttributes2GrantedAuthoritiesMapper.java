package org.springframework.security.authoritymapping;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

import java.util.Locale;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * <p>
 * This class implements the Attributes2GrantedAuthoritiesMapper interface by doing a
 * one-to-one mapping from roles to Spring Security GrantedAuthorities. Optionally a
 * prefix can be added, and the attribute name can be converted to upper or lower
 * case.
 * <p>
 * By default, the attribute is prefixed with "ROLE_" unless it already starts with
 * "ROLE_", and no case conversion is done.
 *
 * @author Ruud Senden
 * @since 2.0
 */
public class SimpleAttributes2GrantedAuthoritiesMapper implements Attributes2GrantedAuthoritiesMapper, InitializingBean {
    private String attributePrefix = "ROLE_";

    private boolean convertAttributeToUpperCase = false;

    private boolean convertAttributeToLowerCase = false;

    private boolean addPrefixIfAlreadyExisting = false;

    /**
     * Check whether all properties have been set to correct values.
     */
    public void afterPropertiesSet() throws Exception {
        Assert.isTrue(!(isConvertAttributeToUpperCase() && isConvertAttributeToLowerCase()),
                "Either convertAttributeToUpperCase or convertAttributeToLowerCase can be set to true, but not both");
    }

    /**
     * Map the given list of string attributes one-to-one to Spring Security GrantedAuthorities.
     */
    public GrantedAuthority[] getGrantedAuthorities(String[] attributes) {
        GrantedAuthority[] result = new GrantedAuthority[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            result[i] = getGrantedAuthority(attributes[i]);
        }
        return result;
    }

    /**
     * Map the given role one-on-one to a Spring Security GrantedAuthority, optionally
     * doing case conversion and/or adding a prefix.
     *
     * @param attribute
     *            The attribute for which to get a GrantedAuthority
     * @return GrantedAuthority representing the given role.
     */
    private GrantedAuthority getGrantedAuthority(String attribute) {
        if (isConvertAttributeToLowerCase()) {
            attribute = attribute.toLowerCase(Locale.getDefault());
        } else if (isConvertAttributeToUpperCase()) {
            attribute = attribute.toUpperCase(Locale.getDefault());
        }
        if (isAddPrefixIfAlreadyExisting() || !attribute.startsWith(getAttributePrefix())) {
            return new GrantedAuthorityImpl(getAttributePrefix() + attribute);
        } else {
            return new GrantedAuthorityImpl(attribute);
        }
    }

    private boolean isConvertAttributeToLowerCase() {
        return convertAttributeToLowerCase;
    }

    public void setConvertAttributeToLowerCase(boolean b) {
        convertAttributeToLowerCase = b;
    }

    private boolean isConvertAttributeToUpperCase() {
        return convertAttributeToUpperCase;
    }

    public void setConvertAttributeToUpperCase(boolean b) {
        convertAttributeToUpperCase = b;
    }

    private String getAttributePrefix() {
        return attributePrefix == null ? "" : attributePrefix;
    }

    public void setAttributePrefix(String string) {
        attributePrefix = string;
    }

    private boolean isAddPrefixIfAlreadyExisting() {
        return addPrefixIfAlreadyExisting;
    }

    public void setAddPrefixIfAlreadyExisting(boolean b) {
        addPrefixIfAlreadyExisting = b;
    }

}