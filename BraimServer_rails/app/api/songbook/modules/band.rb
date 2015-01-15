module Songbook
  module Modules
    class Band < Grape::API

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
          requires :id, type: String, desc: 'Band ID', regexp: /^[[:xdigit:]]{24}$/
        end

      end

      before_validation do
        authenticated_user?
      end

      version :v1 do
        resource :bands do
          # GET
          desc 'returns all existent bands', {
              entity: Songbook::Entities::Band,
              notes: <<-NOTE
                ### Description
                It returns all paginated bands.  by default: page = 1 per_page = 10

                ### Example successful response

                    [
                      {
                        "id": "137a66834fb7802c280000ef",
                        "name": "Foo Figthers",
                        "biography" : "band biography",
                        "genre": "Rock",
                      },
                      {
                        "id": "137a66834fb7802c280000ff",
                        "name": "Dream Theater",
                        "biography" : "band biography",
                        "genre": "Progressive Rock",
                      },
                    ]
              NOTE
            }
          params do
            use :pagination
            use :auth
          end
          get '/', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            bands = ::Band.page(page)
            present bands
          end

          # GET /:id
          desc 'returns a specific band', {
              entity: Songbook::Entities::Band,
              notes: <<-NOTE
                ### Description
                It returns a specific band by its id.

                ### Example successful response

                    {
                      "id": "137a66834fb7802c280000ef",
                      "name": "Foo Figthers",
                      "biography" : "band biography",
                      "genre": "Rock",
                    }

              NOTE
            }
          params do
            use :auth
            use :id
          end
          get '/:id', http_codes: [
            [200, "Successful"],
            [400, "Invalid parameter in entry"],
            [401, "Unauthorized"],
            [404, "Entry not found"],
          ]  do
            content_type "text/json"
            Mongoid.raise_not_found_error = false
            band = ::Band.find(params[:id])
            error!("Document not found for class Entry with id", 404) unless band.present?
            present band, with: Songbook::Entities::Band
          end

          # POST
          desc 'creates a new band', {
              entity: Songbook::Entities::Band,
              notes: <<-NOTE
                ### Description
                It creates a new band record and returns its current representation.

                ### Example successful response

                    {
                      "id": "137a66834fb7802c280000ef",
                      "name": "Foo Figthers",
                      "biography" : "band biography",
                      "genre": "Rock",
                    }

              NOTE
            }
          params do
            use :auth
            optional :band, type: Hash do
              requires :name, type: String, desc: 'Band name', documentation: { example: 'Foo Figthers' }
              requires :genre, type: String, desc: 'Band genre', documentation: { example: 'Rock' }
              optional :biography, type: String, desc: 'Band biography', documentation: { example: 'short bio' }
            end
          end
          post '/', http_codes: [
            [200, "Successful"],
            [400, "Invalid parameter in entry"],
            [401, "Unauthorized"],
            [404, "Entry not found"],
          ]  do
            content_type "text/json"
            band = ::Band.new(params[:band])
            if band.save
              present band, with: Songbook::Entities::Band
            else
              error!(entry.errors, 400)
            end
          end

        end
      end
    end
  end
end
