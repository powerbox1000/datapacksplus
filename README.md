# Datapacks+

Adds some much needed features to datapacks (Work in progress)

## Additions
### Action Registry
The Action Registry allows for datapack devs to easily set up `minecraft:custom` actions that point to a command.
The definition includes a command template and payload schema.
<br>
Example:

```json
{
    "command": "execute as %1 run say %2",
    "payloadSchema": {
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
