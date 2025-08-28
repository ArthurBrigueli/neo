package com.neo.neo.specifications;

import com.neo.neo.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class UserSpecifications {


    public static Specification<User> nameContains(String name){
        return ((root, query, builder) -> {
            if (ObjectUtils.isEmpty(name)) {
                return builder.conjunction();
            }
            return builder.like(root.get("name"), "%" + name + "%");
        });
    }

    public static Specification<User> cpfContains(String cpf){
        return (((root, query, builder) -> {
            if(ObjectUtils.isEmpty(cpf)){
                return builder.conjunction();
            }
            return builder.equal(root.get("cpf"), cpf);
        }));
    }

    public static Specification<User> emailContains(String email){
        return (((root, query, builder) -> {
            if(ObjectUtils.isEmpty(email)){
                return builder.conjunction();
            }

            return builder.equal(root.get("email"), email);
        }));
    }


}
