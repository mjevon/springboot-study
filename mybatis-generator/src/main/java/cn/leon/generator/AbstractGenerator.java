package cn.leon.generator;

import cn.leon.bean.Config;
import cn.leon.bean.TableMetaData;
import cn.leon.constants.GeneratorConstants;
import cn.leon.helper.GeneratorHelper;
import cn.leon.properties.GeneratorProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGenerator implements ICodeGenerator {

    private static final Logger _LOGGER = LoggerFactory.getLogger(AbstractGenerator.class);

    private static final Map<String, String> DATA_TYPE_MAP = new HashMap<>();

    @Autowired
    GeneratorProperties properties;

    @Value("${spring.datasource.name}")
    private String dbName;
    @Value("${spring.datasource.url}")
    protected String url;
    @Value("${spring.datasource.username}")
    protected String username;
    @Value("${spring.datasource.password}")
    protected String password;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    protected Config config;

    static {
        DATA_TYPE_MAP.put("varchar", "String");
        DATA_TYPE_MAP.put("char", "String");
        DATA_TYPE_MAP.put("text", "String");
        DATA_TYPE_MAP.put("int", "int");
        DATA_TYPE_MAP.put("tinyint", "byte");
        DATA_TYPE_MAP.put("smallint", "short");
        DATA_TYPE_MAP.put("mediumint", "short");
        DATA_TYPE_MAP.put("bigint", "java.math.BigInteger");
        DATA_TYPE_MAP.put("float", "float");
        DATA_TYPE_MAP.put("double", "double");
        DATA_TYPE_MAP.put("decimal", "java.math.BigDecimal");
        DATA_TYPE_MAP.put("date", "java.util.Date");
        DATA_TYPE_MAP.put("datetime", "java.util.Date");
        DATA_TYPE_MAP.put("timestamp", "java.util.Date");
        DATA_TYPE_MAP.put("json", "String");
    }

    protected String getJavaFileOutputFolder() {
        return null;
    }

    protected List<String> getAllTableNames() throws Exception {
        return jdbcTemplate.queryForList(GeneratorConstants.InformationSchemaSql.INFORMATION_SCHEMA_TABLES, String.class, dbName);
    }

    protected List<String> getColumnNames(List<TableMetaData> data) throws Exception {
        List<String> columns = new ArrayList<>();
        data.stream().forEach(d -> columns.add(d.getColumnName()));
        return columns;

    }

    protected List<TableMetaData> getTableMetaData(String table) throws Exception {
        return jdbcTemplate.query(GeneratorConstants.InformationSchemaSql.INFORMATION_SCHEMA_COLUMNS, (rs, i) -> {
            TableMetaData data = new TableMetaData();
            data.setColumnName(rs.getString(GeneratorConstants.SchemaColumnName.COLUMN_NAME));
            data.setComment(StringUtils.trimToEmpty(rs.getString(GeneratorConstants.SchemaColumnName.COLUMN_COMMENT)));
            data.setDataType(rs.getString(GeneratorConstants.SchemaColumnName.DATA_TYPE).toLowerCase());
            data.setPrimaryKey(GeneratorConstants.SchemaColumnName.PRI.equalsIgnoreCase(rs.getString(GeneratorConstants.SchemaColumnName.COLUMN_KEY)));
            data.setName(rs.getString(GeneratorConstants.SchemaColumnName.COLUMN_NAME));
            String len, scale = null;
            do {
                len = rs.getString(GeneratorConstants.SchemaColumnName.CHARACTER_MAXIMUM_LENGTH);
                if (len != null) {
                    break;
                }
                len = rs.getString(GeneratorConstants.SchemaColumnName.NUMERIC_PRECISION);
                if (len != null) {
                    scale = rs.getString(GeneratorConstants.SchemaColumnName.NUMERIC_SCALE);
                }
            } while (false);
            Integer length = GeneratorHelper.convertToInt(len);
            if (StringUtils.isNotBlank(scale)) {
                length = length + GeneratorHelper.convertToInt(scale) + 1;
            }
            data.setLength(length);
            return data;
        }, table, dbName);
    }

    protected String getJavaTypeName(TableMetaData d) {
        String type = DATA_TYPE_MAP.get(StringUtils.lowerCase(d.getDataType()));
        if (d.getComment().contains("#") && "json".equalsIgnoreCase(d.getDataType())) {
            type = "short";
        }
        return type;
    }

}
