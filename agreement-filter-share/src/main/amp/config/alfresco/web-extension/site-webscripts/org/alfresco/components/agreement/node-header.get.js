function main()
{
   var sharedId = args.sharedId,
      result = remote.connect("alfresco-noauth").get("/api/internal/shared/node/" + encodeURIComponent(sharedId) + "/metadata");

   if (result.status == 200)
   {
      var nodeMetadata = JSON.parse(result);

      model.fileExtension = "generic";
      var i = nodeMetadata.name.lastIndexOf(".");
      if (i > -1)
      {
         model.fileExtension = nodeMetadata.name.substring(i + 1);
      }

      model.displayName = nodeMetadata.name;

   }

}

main();
