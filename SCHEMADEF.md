```json
{
    "command": "command goes here",
    "payloadSchema": {
        // The NBT types are byte, float, short, int, long, float, double, string, list[], compound, byte[], int[], or long[]
        // The array types are not currently supported
        "foo": {
            "type": "string",
            "mapping": "%1"
        },
        "bar": {
            "type": "compound",
            "children": {
                "foobar": {
                    "type": "int",
                    "mapping": "%2"
                }
            }
        }
    }
}
```