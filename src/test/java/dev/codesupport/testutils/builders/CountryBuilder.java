package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.CountryEntity;
import dev.codesupport.web.domain.Country;

//unused - Used for unit tests, not everything will be used
@SuppressWarnings("unused")
public class CountryBuilder {

    private Long id;
    private String code;
    private String label;

    private CountryBuilder() {

    }

    public static CountryBuilder builder() {
        return new CountryBuilder();
    }

    public Country buildDomain() {
        Country domain = new Country();
        domain.setId(id);
        domain.setCode(code);
        domain.setLabel(label);
        return domain;
    }

    public CountryEntity buildEntity() {
        CountryEntity entity = new CountryEntity();
        entity.setId(id);
        entity.setCode(code);
        entity.setLabel(label);
        return entity;
    }

    public CountryBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public CountryBuilder code(String code) {
        this.code = code;
        return this;
    }

    public CountryBuilder label(String label) {
        this.label = label;
        return this;
    }
}
