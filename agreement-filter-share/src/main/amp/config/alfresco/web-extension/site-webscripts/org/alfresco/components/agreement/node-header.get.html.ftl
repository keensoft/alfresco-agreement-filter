<@standalone>
   <@markup id="css" >
      <#-- CSS Dependencies -->
      <@link rel="stylesheet" type="text/css" href="${url.context}/res/components/quickshare/node-header.css" />
   </@>

   <@markup id="js"/>

   <@markup id="widgets"/>

   <@markup id="html">
      <@uniqueIdDiv>
         <#assign el=args.htmlid?html/>
         <div class="quickshare-node-header">

            <#-- Icon -->
            <img src="${url.context}/res/components/images/filetypes/${fileExtension}-file-48.png"
                 onerror="this.src='${url.context}/res/components/images/filetypes/generic-file-48.png'"
                 title="${displayName?html}" class="quickshare-node-header-info-thumbnail" width="48" />

            <#-- Title -->
            <h1 class="quickshare-node-header-info-title thin dark">${displayName?html}</h1>

         </div>
      </@>
   </@>
</@>