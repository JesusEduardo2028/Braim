module Songbook			
	module Modules
		class Song < Grape::API
			format :json
			content_type :json, 'application/json'
			helpers do 
				params :pagination do 
					optional :page, type: Integer
					optional :per_page, type: Integer
				end

				params :auth do 
					requires :songbook_token, type: String, desc: 'Auth token', documentation: {example: '837f6b854fc7802c2800302e'}
				end

				params :id do
		      requires :id, type: String, desc: 'Band ID', regexp: /^[[:xdigit:]]{24}$/
		    end
		  end
	    
	    before_validation do
	      #authenticated_user?
	    end

	    version :v1 do
        resource :songs do
          # GET
          desc 'returns all existent songs', {
              entity: Songbook::Entities::Song,
              notes: <<-NOTE
                ### Description
                  It returns all paginated songs.  by default: page = 1 per_page = 10

                ### Example successful response

                    [
                      {
                        "id": "137a66834fb7802c280000ef",
                        "name": "All my life",
                        "duration" : 300000,
                        "url": "http://braimserver/songs/all_life.mp3",
                      },
                      {
                        "id": "137a66834fb7802c280000ef",
                        "name": "Rain of consecuenses",
                        "duration" : 330000,
                        "url": "http://braimserver/songs/rain_of_consecuenses.mp3",
                      },
                    ]
              NOTE
            }
          params do
            use :pagination
            #use :auth
          end

          get '/', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            songs = ::Song.page(page)
            present songs
          end
        end
     	end
    end
  end
end