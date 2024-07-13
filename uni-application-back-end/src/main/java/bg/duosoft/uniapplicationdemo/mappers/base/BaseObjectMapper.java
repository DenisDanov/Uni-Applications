package bg.duosoft.uniapplicationdemo.mappers.base;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

public abstract class BaseObjectMapper<E, C> {
    public BaseObjectMapper() {
    }

    public abstract C toDto(E e);

    @InheritConfiguration(
            name = "toDto"
    )
    @IterableMapping(
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
    )
    public abstract List<C> toDtoList(List<E> eList);

    @InheritInverseConfiguration(
            name = "toDto"
    )
    public abstract E toEntity(C c);

    @InheritInverseConfiguration(
            name = "toDtoList"
    )
    @IterableMapping(
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
    )
    public abstract List<E> toEntityList(List<C> cList);
}
