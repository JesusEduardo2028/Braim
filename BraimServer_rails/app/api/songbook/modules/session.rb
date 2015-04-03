module Songbook
  module Modules
    class Session < Grape::API
      
      format :json

      content_type :json, 'application/json'

      helpers do
        # Pagination stuff, unfortunately it cannot be located in API root class
        params :pagination do
          optional :page, type: Integer
          optional :per_page, type: Integer
        end

        params :auth do
          requires :braim_token, type: String, desc: 'Auth token', documentation: { example: '837f6b854fc7802c2800302e' }
        end

        params :id do
          requires :id, type: String, desc: 'User ID', regexp: /^[[:xdigit:]]{24}$/
        end

      end

      before_validation do
        authenticated_user?
      end

      version :v1 do
        resource :sessions do
          desc 'returns all existent sessions', {
            entity: Songbook::Entities::Session,
            notes: <<-NOTES
              Returns all existent sessions paginated
            NOTES
          }
          params do
            use :auth
            use :pagination
          end
          get '/', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            sessions = ::EmoSession.page(page)
            header 'total_pages', sessions.total_pages.to_s
            present sessions, with: Songbook::Entities::Session
          end
          desc 'returns one existent session by :id', {
            entity: Songbook::Entities::Session,
            notes: <<-NOTES
              Returns one existent session by :id
            NOTES
          }
          params do
            use :auth
            use :id
          end
          get '/:id', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            session = ::EmoSession.find(params[:id])
            present session, with: Songbook::Entities::Session
          end
          desc 'returns all emotional entries from a given session', {
            entity: Songbook::Entities::Session,
            notes: <<-NOTES
               ### Description
                It returns all emotional entries from a session

                ### Example successful response
                    [
                      {
                        emo entry item
                      },
                        emo entry item
                      {
                    ]
            NOTES
          }
          params do
            use :auth
            use :id
            use :pagination
          end
          get '/:id/emo_entries', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            session = ::EmoSession.find(params[:id])
            emo_entries = session.emo_entries.page(page)
            header 'total_pages', emo_entries.total_pages.to_s
            present emo_entries, with:  Songbook::Entities::EmoEntry
          end
          desc 'returns all player entries from a given session', {
            entity: Songbook::Entities::Session,
            notes: <<-NOTES
               ### Description
                It returns all player entries from a session

                ### Example successful response
                    [
                      {
                        player entry item
                      },
                        player entry item
                      {
                    ]
            NOTES
          }
          params do
            use :auth
            use :id
            use :pagination
          end
          get '/:id/player_entries', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            session = ::EmoSession.find(params[:id])
            player_entries = session.player_entries.page(page)
            header 'total_pages', player_entries.total_pages.to_s
            present player_entries, with:  Songbook::Entities::PlayerEntry
          end

          # POST
          desc 'creates a new session', {
              entity: Songbook::Entities::Band,
              notes: <<-NOTE
                ### Description
                It creates a new band record and returns its current representation.

                ### Example successful response

                    {
                      "id": "137a66834fb7802c280000ef",
                      

                    }

              NOTE
            }
          params do
            use :auth
            optional :session, type: Hash do
              requires :user_id, type: String, desc: 'User id of user that create the session', documentation: { example: 'Rock' }
            end
          end
          post '/', http_codes: [
            [200, "Successful"],
            [400, "Invalid parameter in entry"],
            [401, "Unauthorized"],
            [404, "Entry not found"],
          ]  do
            content_type "text/json"
            session_params = params[:session]
            user = ::User.find(session_params['user_id'])
            session = user.emo_sessions.build
            if session.save
              present session, with: Songbook::Entities::Session
            else
              error!(entry.errors, 400)
            end
          end


        end
      end
    end
  end
end
