package org.myframework.extra.jackson;

import cn.hutool.core.lang.Opt;
import cn.hutool.log.StaticLog;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.myframework.extra.dict.EnumDict;

import java.io.IOException;

import static cn.hutool.core.text.CharSequenceUtil.format;

public class JKDictFormatSerializer extends StdSerializer<Object> {

    protected JKDictFormatSerializer() {
        super(Object.class);
        StaticLog.trace("JKDictFormat 初始化完成，字典序列器已注入");
    }

    @Override
    public void serialize(Object value,
                          JsonGenerator gen,
                          SerializerProvider provider) throws IOException {
        if (!(value instanceof EnumDict<?> enumDict)) {
            provider.defaultSerializeValue(value, gen);
        } else {
            var currentName = Opt.ofNullable(gen)
                    .map(JsonGenerator::getOutputContext)
                    .map(JsonStreamContext::getCurrentName)
                    .get();
            gen.writeObject(enumDict.getValue());
            gen.writeFieldName(format("{}Tag", currentName));
            gen.writeStartObject();
            gen.writeFieldName("label");
            gen.writeString(enumDict.getLabel());
            gen.writeFieldName("value");
            gen.writeObject(enumDict.getValue());
            gen.writeFieldName("style");
            gen.writeNumber(enumDict.getStyle());
            gen.writeEndObject();
        }
    }
}
