package bg.duosoft.uniapplicationdemo.models.dtos.base;

import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class BaseDTO<ID extends Serializable> implements Serializable {
    private ID id;
}
