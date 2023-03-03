package com.chuwa.redbook.validator;

import com.chuwa.redbook.payload.PostDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EitherOrValidator implements ConstraintValidator<EitherOr, PostDto> {

    @Override
    public void initialize(EitherOr arg0) { }

    @Override
    public boolean isValid(PostDto postDto, ConstraintValidatorContext constraintValidatorContext) {
        return ((postDto.getPictures() != null && postDto.getPictures().size() != 0) && postDto.getVideo() == null)
                || ((postDto.getPictures() == null || postDto.getPictures().size() == 0) && postDto.getVideo() != null);
    }
}
