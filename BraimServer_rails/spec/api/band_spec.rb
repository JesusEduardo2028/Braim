describe 'Band', :type => :request do

  describe :v1 do

    context 'bands', :keep_db do

      before :context do
        @user = FactoryGirl.create :user, email: 'allam.britto@fake.com', password: '12345678', password_confirmation: '12345678'
        FactoryGirl.create_list(:band, 20)
        get '/api/v1/token', %Q{email=allam.britto@fake.com&password=12345678}
        @credentials = JSON.parse response.body
      end

      context 'GET' do

        it 'verifies that response has the elements number specified in per_page param' do
          token = "songbook_token=#{@credentials['access_token']}"
          per_page = 5
          data = "per_page=#{per_page}"

          get '/api/v1/bands', "#{token}&#{data}"

          expect(response.status).to eq 200
          expect(JSON.parse(response.body).count).to be per_page
        end

        context '/:id' do

          it 'gets band by id' do

            band = Band.last

            expected_response = {
              id: band.id.to_s,
              name: band.name,
              biography: band.biography,
              genre: band.genre
            }

            get "/api/v1/bands/#{band.id.to_s}", songbook_token: @credentials['access_token']
            expect(response.status).to eq 200
            expect(JSON.parse(response.body)).to match expected_response.stringify_keys
          end

        end

      end

      context 'POST' do
        it 'returns a representation of new band and code 201' do
          new_band = Band.new name: 'Superlitio', genre: 'Rock', biography: 'is a Latin rock band from Cali, Colombia formed in 1997. Its members are Pedro Rovetto (bass), Pipe Bravo (Lead Voice, keyboards and guitar), Alejandro Lozano (guitar), Armando Gonzalez (drums) and Dino Leandro (Machines and Ukulele).'

          token = "songbook_token=#{@credentials['access_token']}"
          data = "band[name]=#{new_band.name}&band[biography]=#{new_band.biography}&band[genre]=#{new_band.genre}"

          expected_response = {
            name: new_band.name,
            biography: new_band.biography,
            genre: new_band.genre
          }

          post '/api/v1/bands', "#{token}&#{data}"

          expect(response.status).to eq 201
          expect(JSON.parse(response.body).except('id')).to match(expected_response.stringify_keys)
        end
      end

    end

  end

end
