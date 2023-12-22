package bartnik.master.app.relational.recipeforum.util;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

@Component
public class UuidConverter implements Converter<byte[], UUID> {

    @Override
    public UUID convert(byte[] source) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(source);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();
        return new UUID(high, low);
    }
}
