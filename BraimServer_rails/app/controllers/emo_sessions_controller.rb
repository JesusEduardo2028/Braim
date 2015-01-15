class EmoSessionsController < ApplicationController
  before_action :set_emo_session, only: [:show, :edit, :update, :destroy , :raw, :affectiv]

  respond_to :html

  def index
    @emo_sessions = EmoSession.all
    respond_with(@emo_sessions)
  end

  def show
    respond_with(@emo_session)
  end

  def raw
    @emo_entries = @emo_session.emo_entries
    @player_entries = @emo_session.player_entries 
    @data_node_0 = @emo_entries.map {|entry| [entry.timestamp.to_i,entry.nodes[0]]}
    @data_node_1 = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[1]]}
    @data_node_2 = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[2]]}
    @data_node_3 = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[3]]}
    @data_node_4 = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[4]]}
    @data_node_5 = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[5]]}
    @data_node_6 = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[6]]}
    @data_node_7 = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[7]]}
    @data_node_8 = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[8]]}
    @data_node_9 = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[9]]}
    @data_node_10 = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[10]]}
    @data_node_11 = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[11]]}
    @data_node_12 = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[12]]}
    @data_node_13 = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[13]]}
    @player_timeline = @player_entries.map {|entry| [entry.timestamp.to_f,entry.player_state]}
    render :show
  end

  def affectiv
    @emo_entries = @emo_session.emo_entries
    @player_entries = @emo_session.player_entries 
    @data_excitement = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.excitement.to_f]}
    @data_frustration = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.frustration.to_f]}
    @data_meditation = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.meditation.to_f]}
    @data_engagement = @emo_entries.map {|entry| [entry.timestamp.to_f,entry.engagement.to_f]}
    @player_timeline = @player_entries.map {|entry| [entry.timestamp.to_f,entry.player_state]}
    render :show
  end

  def new
    @emo_session = EmoSession.new
    respond_with(@emo_session)
  end

  def edit
  end

  def create
    @emo_session = EmoSession.new(emo_session_params)
    @emo_session.save
    respond_with(@emo_session)
  end

  def update
    @emo_session.update(emo_session_params)
    respond_with(@emo_session)
  end

  def destroy
    @emo_session.destroy
    respond_with(@emo_session)
  end

  private
    def set_emo_session
      @emo_session = EmoSession.find(params[:id])
    end

    def emo_session_params
      params[:emo_session]
    end
end
