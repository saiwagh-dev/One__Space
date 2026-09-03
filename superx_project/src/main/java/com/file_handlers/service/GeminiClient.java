package com.file_handlers.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.file_handlers.model.SpaceData;

public class GeminiClient{

    private static final String MODEL="gemini-3.6-flash";

    private static final String API_URL=
            "https://generativelanguage.googleapis.com/v1beta/models/"
                    +MODEL
                    +":generateContent";

    private final String apiKey;
    private final HttpClient client;

    public GeminiClient(){

        apiKey=System.getenv(
                "GEMINI_API_KEY"
        );

        if(apiKey==null||apiKey.isBlank())
            throw new IllegalStateException(
                    "GEMINI_API_KEY environment variable is not set."
            );

        client=
                HttpClient.newBuilder()
                        .connectTimeout(
                                Duration.ofSeconds(15)
                        )
                        .build();
    }

    public String classify(
            String fileName,
            String extractedText,
            List<SpaceData> customSpaces
    )throws IOException,InterruptedException{

        String prompt=
                "Analyze this file for OneSpace.\n"
                +"File name: "
                +fileName
                +"\n"
                +"Content:\n"
                +(extractedText==null
                        ?""
                        :extractedText)
                +"\n\n"

                +"Return ONLY valid JSON. "
                +"Do not return markdown or explanations.\n\n"

                +"CLASSIFICATION:\n"
                +"By default, use exactly one category from: "
                +"Personal, College, Office, Finance, Entertainment, Other.\n"

                +buildCustomSpacesBlock(
                        customSpaces
                )

                +"confidence must be between 0 and 1.\n"

                +"description must be a short meaningful description "
                +"of the file in 2-4 sentences.\n"

                +"smartTags must contain exactly 5 or 6 useful short tags.\n\n"

                +"IMPORTANT EVENT EXTRACTION:\n"
                +"Look for important dates and markable events explicitly "
                +"stated or clearly supported by the file.\n"

                +"Examples include:\n"
                +"- assignment deadlines\n"
                +"- submission deadlines\n"
                +"- examination dates\n"
                +"- project presentations\n"
                +"- meetings\n"
                +"- appointments\n"
                +"- payment due dates\n"
                +"- bill due dates\n"
                +"- policy expiry dates\n"
                +"- renewal dates\n"
                +"- subscription expiry dates\n"
                +"- application deadlines\n"
                +"- interview dates\n"
                +"- scheduled tasks\n"
                +"- important event dates\n"

                +"Do NOT invent dates.\n"
                +"Only extract dates that are explicitly present "
                +"or clearly supported by the supplied content.\n"

                +"If the file contains no important dates or events, "
                +"return an empty extractedEvents array.\n"

                +"Return at most 5 important events.\n\n"

                +"Each extracted event must contain:\n"
                +"- title\n"
                +"- date in YYYY-MM-DD format\n"
                +"- type: deadline, task, or event\n"
                +"- description\n\n"

                +"Required JSON format:\n"

                +"{"
                +"\"category\":\"Office\","
                +"\"confidence\":0.95,"
                +"\"description\":\"Short description.\","
                +"\"smartTags\":["
                +"\"tag1\","
                +"\"tag2\","
                +"\"tag3\","
                +"\"tag4\","
                +"\"tag5\""
                +"],"
                +"\"extractedEvents\":["
                +"{"
                +"\"title\":\"Project Deadline\","
                +"\"date\":\"2026-09-15\","
                +"\"type\":\"deadline\","
                +"\"description\":\"Submit the project.\""
                +"}"
                +"]"
                +"}";

        return extractJson(
                sendRequest(prompt)
        );
    }

    private String buildCustomSpacesBlock(
            List<SpaceData> customSpaces
    ){

        if(customSpaces==null||
                customSpaces.isEmpty()){

            return "";
        }

        StringBuilder block=
                new StringBuilder();

        block.append(
                "\nThe user has also created these custom Spaces. "
        );

        block.append(
                "If the file's content, purpose or subject matter "
                +"fits one of these custom Spaces better than the "
                +"fixed categories above, return that Space's name "
                +"EXACTLY as written as the category instead of a "
                +"fixed category:\n"
        );

        for(SpaceData space:customSpaces){

            if(space==null||
                    space.getName()==null||
                    space.getName().isBlank()){

                continue;
            }

            String description=
                    space.getDescription();

            block.append(
                    "- \""
                            +space.getName()
                            +"\": "
            );

            block.append(
                    description==null||
                            description.isBlank()
                            ?"No description provided."
                            :description
            );

            List<String> tags=
                    space.getTags();

            if(tags!=null&&!tags.isEmpty()){

                block.append(
                        " (keywords: "
                                +String.join(
                                        ", ",
                                        tags
                                )
                                +")"
                );
            }

            block.append("\n");
        }

        return block.toString();
    }

    public String chat(
            String userMessage,
            String conversationContext,
            String fileContext
    )throws IOException,InterruptedException{

        String prompt=
                "You are OneSpace AI, a helpful assistant inside "
                +"the user's OneSpace application.\n\n"

                +"You are NOT a general-purpose chatbot.\n"
                +"You may only answer questions related to OneSpace, "
                +"the user's OneSpace files, Spaces, Calendar, "
                +"Search, reminders, storage, or other OneSpace "
                +"application functionality.\n\n"

                +"If the user's question is unrelated to OneSpace, "
                +"respond exactly with:\n"
                +"I'm OneSpace AI, so I can only help with your "
                +"OneSpace files, Spaces, and application.\n\n"

                +"For OneSpace questions, answer naturally, clearly "
                +"and concisely.\n"

                +"You may use the conversation context for continuity.\n\n"

                +"IMPORTANT RULES FOR ONESPACE FILES:\n"
                +"1. The section called Relevant OneSpace Files "
                +"contains information extracted from the user's files.\n"

                +"2. If the user's question is about their personal "
                +"files, use the supplied file information as the "
                +"primary source.\n"

                +"3. Do not invent, guess or assume information that "
                +"is not present in the supplied file information.\n"

                +"4. If the requested information is not available "
                +"in the supplied files, clearly say that you could "
                +"not find it in the available OneSpace files.\n"

                +"5. When answering a file-related question, mention "
                +"the relevant file name when useful.\n"

                +"6. For sensitive values such as identification "
                +"numbers, policy numbers, account numbers or dates, "
                +"only provide them when explicitly present in the "
                +"supplied file content.\n"

                +"7. Do not claim that you accessed a file directly. "
                +"You only have access to the extracted information "
                +"provided below.\n\n"

                +"Relevant OneSpace Files:\n"
                +(fileContext==null||
                        fileContext.isBlank()
                        ?"No relevant files were found."
                        :fileContext)

                +"\nConversation Context:\n"
                +(conversationContext==null||
                        conversationContext.isBlank()
                        ?"No previous conversation."
                        :conversationContext)

                +"\n\nUser Question:\n"
                +userMessage

                +"\n\nAssistant:";

        return sendRequest(prompt);
    }

    private String sendRequest(
            String prompt
    )throws IOException,InterruptedException{

        JSONObject textPart=
                new JSONObject();

        textPart.put(
                "text",
                prompt
        );

        JSONObject content=
                new JSONObject();

        content.put(
                "parts",
                new JSONArray().put(textPart)
        );

        JSONObject body=
                new JSONObject();

        body.put(
                "contents",
                new JSONArray().put(content)
        );

        HttpRequest request=
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        API_URL
                                                +"?key="
                                                +apiKey
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .timeout(
                                Duration.ofSeconds(60)
                        )
                        .POST(
                                HttpRequest.BodyPublishers.ofString(
                                        body.toString(),
                                        StandardCharsets.UTF_8
                                )
                        )
                        .build();

        HttpResponse<String> response=
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if(response.statusCode()!=200){

            throw new IOException(
                    "Gemini request failed: "
                            +response.statusCode()
                            +" "
                            +response.body()
            );
        }

        JSONObject root=
                new JSONObject(
                        response.body()
                );

        String result=
                root.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                        .trim();

        return result;
    }

    private String extractJson(String text){

        if(text==null||text.isBlank())
            return "{}";

        int start=text.indexOf('{');
        int end=text.lastIndexOf('}');

        if(start>=0&&end>start)
            return text.substring(
                    start,
                    end+1
            ).trim();

        return text.trim();
    }
}