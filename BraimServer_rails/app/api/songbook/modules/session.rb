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
          requires :songbook_token, type: String, desc: 'Auth token', documentation: { example: '837f6b854fc7802c2800302e' }
        end

        params :id do
          requires :id, type: String, desc: 'User ID', regexp: /^[[:xdigit:]]{24}$/
        end

      end

      before_validation do
        #authenticated_user?
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
            #use :auth
            use :pagination
          end
          get '/', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            sessions = ::EmoSession.page(page)
            present sessions, with: Songbook::Entities::Session
          end
          desc 'returns one existent session by :id', {
            entity: Songbook::Entities::Session,
            notes: <<-NOTES
              Returns one existent session by :id
            NOTES
          }
          params do
            #use :auth
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
            #use :auth
            use :id
          end
          get '/:id/emo_entries', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            session = ::EmoSession.find(params[:id])
            present session.emo_entries, with:  Songbook::Entities::EmoEntry
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
            #use :auth
            use :id
          end
          get '/:id/player_entries', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            session = ::EmoSession.find(params[:id])
            present session.player_entries, with:  Songbook::Entities::PlayerEntry
          end
        end
      end
    end
  end
end
