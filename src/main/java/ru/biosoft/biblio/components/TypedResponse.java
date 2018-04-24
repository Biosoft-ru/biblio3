package ru.biosoft.biblio.components;

public class TypedResponse
{
    public String type;
    public String message;
    public Object data;

    public static TypedResponse error(String message)
    {
        return new TypedResponse("error", message, null);
    }

    public static TypedResponse data(Object data)
    {
        return new TypedResponse("ok", null, data);
    }

    private TypedResponse(String type, String message, Object data)
    {
        this.type = type;
        this.message = message;
        this.data = data;
    }
}
