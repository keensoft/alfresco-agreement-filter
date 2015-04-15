function getHomeUrl(userId) {
	// Setting agreement acceptance
    var connector = remote.connect("alfresco");
    var result = connector.post("/keensoft/agreement/" + userId, '', "application/json");
    return url.context;
}

function main()
{
   model.redirectUrl = getHomeUrl(user.name);
}

main();
