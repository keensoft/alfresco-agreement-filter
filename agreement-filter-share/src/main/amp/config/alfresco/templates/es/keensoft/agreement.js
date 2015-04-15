function main()
{
   var result = remote.connect("alfresco-noauth").get("/keensoft/agreement-page");
   if (result.status == 200) {
	   
	   var json = JSON.parse(result);
	   var sharedId = json.sharedId;
	   context.attributes.sharedId = sharedId;
	   
	   result = remote.connect("alfresco-noauth").get("/api/internal/shared/node/" + encodeURIComponent(sharedId) + "/metadata");
	   model.outcome = result.status == 200 ? "" : "error";
	   
   } else {
	   
	   model.outcome = "error";
	   
   }

}

main();
